package ch.uzh.marugoto.core.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.marugoto.core.data.entity.Component;
import ch.uzh.marugoto.core.data.entity.Exercise;
import ch.uzh.marugoto.core.data.entity.ExerciseState;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.PageState;
import ch.uzh.marugoto.core.data.entity.PageTransition;
import ch.uzh.marugoto.core.data.entity.PageTransitionState;
import ch.uzh.marugoto.core.data.entity.StorylineState;
import ch.uzh.marugoto.core.data.entity.User;
import ch.uzh.marugoto.core.data.repository.ExerciseStateRepository;
import ch.uzh.marugoto.core.data.repository.PageStateRepository;
import ch.uzh.marugoto.core.data.repository.PageTransitionRepository;
import ch.uzh.marugoto.core.data.repository.PageTransitionStateRepository;
import ch.uzh.marugoto.core.data.repository.StorylineStateRepository;
import ch.uzh.marugoto.core.data.repository.UserRepository;

/**
 * State service - responsible for application states
 */

@Service
public class StateService {

	@Autowired
	private StorylineStateRepository storylineStateRepository;

	@Autowired
	private PageStateRepository pageStateRepository;

	@Autowired
	private ExerciseStateRepository exerciseStateRepository;

	@Autowired
	private PageTransitionStateRepository pageTransitionStateRepository;

	@Autowired
	private PageTransitionRepository pageTransitionRepository;

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Returns the current storylineState from the current user And initials the
	 * story line if needed
	 * 
	 * @param current user
	 * @return storylineState
	 */
	public StorylineState getStorylineState(User user, Page page) {
		StorylineState storylineState = user.getCurrentlyPlaying();
		
		if (page.getStartsStoryline() != null) {
			if (storylineState != null) {
				storylineState.setFinishedAt(LocalDateTime.now());
				storylineStateRepository.save(storylineState);
			}
			storylineState = new StorylineState(page.getStartsStoryline(), getPageState(page, user), user);
			storylineState.setStartedAt(LocalDateTime.now());
			storylineStateRepository.save(storylineState);

			user.setCurrentlyPlaying(storylineState);
			userRepository.save(user);
		}

		return storylineState;
	}

	/**
	 * Finds the page state for the page and user
	 * 
	 * @param page
	 * @param user
	 * @return pageState
	 */
	public PageState getPageState(Page page, User user) {
		PageState pageState = pageStateRepository.findByPageAndUser(page.getId(), user.getId());

		if (pageState == null) {
			pageState = new PageState(page, user);
			pageState.setPageTransitionStates(createPageTransitionStates(page));
			pageStateRepository.save(pageState);
		}

		return pageState;
	}
	
	/**
	 * Creates pageTransitionState and add it to the list
	 * 
	 * @param page
	 * @return pageTransitionStates
	 */
	private List<PageTransitionState> createPageTransitionStates(Page page) {
		List<PageTransition> pageTransitions = pageTransitionRepository.findByPageId(page.getId());
		List<PageTransitionState> pageTransitionStates = new ArrayList<PageTransitionState>();

		for (PageTransition pageTransition : pageTransitions) {
			var pageTransitionState = new PageTransitionState(true, pageTransition);
			pageTransitionStateRepository.save(pageTransitionState);
			pageTransitionStates.add(pageTransitionState);
		}

		return pageTransitionStates;
	}

	/**
	 * Finds all exercise states for the PageState
	 * 
	 * @param pageState
	 * @return exerciseStates
	 */
	public List<ExerciseState> getExerciseStates(PageState pageState) {
		List<ExerciseState> exerciseStates = exerciseStateRepository.findByPageState(pageState);

		if (exerciseStates.isEmpty()) {
			// create exercise states
			for (Component component : pageState.getPage().getComponents()) {
				if (component instanceof Exercise) {
					ExerciseState exerciseState = exerciseStateRepository.save(new ExerciseState((Exercise) component));
					exerciseStates.add(exerciseState);
				}
			}
		}

		return exerciseStates;
	}

	/**
	 * Updates states after user page transition is done
	 * 
	 * @param chosenByPlayer
	 * @param pageTransition
	 * @param user
	 */
	public void updateStatesAfterTransition(boolean chosenByPlayer, PageTransition pageTransition, User user) {
		PageState fromPageState = getPageState(pageTransition.getFrom(), user);
		fromPageState.setLeftAt(LocalDateTime.now());
		fromPageState.getPageTransitionState(pageTransition).setChosenByPlayer(chosenByPlayer);
		pageStateRepository.save(fromPageState);

		PageState toPageState = getPageState(pageTransition.getTo(), user);
		toPageState.setEnteredAt(LocalDateTime.now());
		pageStateRepository.save(toPageState);

		StorylineState storylineState = getStorylineState(user, pageTransition.getTo());
		storylineState.setCurrentlyAt(toPageState);
		storylineState.setLastSavedAt(LocalDateTime.now());
		storylineStateRepository.save(storylineState);
	}

	/**
	 * Updates exercise state
	 * 
	 * @param exerciseStateId
	 * @param inputText
	 * @return ExerciseState
	 */
	public ExerciseState updateExerciseState(String exerciseStateId, String inputState) {
		ExerciseState exerciseState = exerciseStateRepository.findById(exerciseStateId).get();
		exerciseState.setInputState(inputState);
		exerciseStateRepository.save(exerciseState);
		return exerciseState;
	}
}
