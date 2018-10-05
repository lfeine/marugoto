package ch.uzh.marugoto.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.marugoto.core.data.entity.Criteria;
import ch.uzh.marugoto.core.data.entity.ExerciseCriteriaType;
import ch.uzh.marugoto.core.data.entity.ExerciseState;
import ch.uzh.marugoto.core.data.entity.Money;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.PageTransition;
import ch.uzh.marugoto.core.data.entity.User;
import ch.uzh.marugoto.core.data.entity.VirtualTime;
import ch.uzh.marugoto.core.data.repository.ComponentRepository;
import ch.uzh.marugoto.core.data.repository.PageRepository;
import ch.uzh.marugoto.core.data.repository.PageTransitionRepository;
import ch.uzh.marugoto.core.exception.PageTransitionNotAllowedException;

/**
 * PageService provides functionality related to page and pageTransition entities.
 */
@Service
public class PageService {

	@Autowired
	private PageRepository pageRepository;
	
	@Autowired
	private PageTransitionRepository pageTransitionRepository;
	
	@Autowired
	private StateService stateService;

	@Autowired
	private ComponentService componentService;


	/**
	 * Get page with all the belonging components
	 * 
	 * @param id
	 * @return Page
	 */
	public Page getPage(String id) {
        Page page = pageRepository.findById(id).orElseThrow();
		page.setPageTransitions(getPageTransitions(page));
		return page;
	}

	/**
	 * Transition: from page - to page
	 * Updates previous page states and returns next page
	 *
	 * @param chosenByPlayer
	 * @param pageTransitionId
	 * @param user
	 * @return nextPage
	 */
	public Page doTransition(boolean chosenByPlayer, String pageTransitionId, User user) throws PageTransitionNotAllowedException {
		PageTransition pageTransition = pageTransitionRepository.findById(pageTransitionId).orElseThrow();

		if (!isPageTransitionAllowed(pageTransition, user)) {
			throw new PageTransitionNotAllowedException();
		}

		Page nextPage = pageTransition.getTo();

		if (pageTransition.getVirtualTime() != null) {
			Duration currentTime = pageTransition.getFrom().getVirtualTime().getTime();
			nextPage.setVirtualTime(new VirtualTime(currentTime.plus(pageTransition.getVirtualTime().getTime()) , true));
		}
		if (pageTransition.getMoney() != null) {
			double currentMoney = pageTransition.getFrom().getMoney().getAmount();
			nextPage.setMoney(new Money(currentMoney + pageTransition.getMoney().getAmount()));
		}
		
		stateService.updateStatesAfterTransition(chosenByPlayer, pageTransition, user);
		nextPage.setPageTransitions(getAllowedPageTransitions(nextPage, user));

		return nextPage;
	}

	/**
	 * Returns all page transitions
	 *
	 * @param page
	 * @return pageTransitions
	 */
	private List<PageTransition> getPageTransitions(Page page) {
		return pageTransitionRepository.findByPageId(page.getId());
	}

	/**
	 * Returns allowed page transitions for user
	 *
	 * @param page
	 * @param user
	 * @return allowedPageTransitions
	 */
	private List<PageTransition> getAllowedPageTransitions(Page page, User user) {
		List<PageTransition> allowedPageTransitions = new ArrayList<>();

		for (PageTransition pageTransition : getPageTransitions(page)) {
			if (isPageTransitionAllowed(pageTransition, user)) {
				allowedPageTransitions.add(pageTransition);
			}
		}

		return allowedPageTransitions;
	}

	/**
	 * Checks if page transition is allowed for user
	 *
	 * @param pageTransition
	 * @param user
	 * @return allowed
	 */
	private boolean isPageTransitionAllowed(PageTransition pageTransition, User user) {
		boolean allowed = true;

		if (!pageTransition.getCriteria().isEmpty()) {
			for (Criteria criteria : pageTransition.getCriteria()) {
				if (criteria.isForExercise()) {
					ExerciseState exerciseState = stateService.getExerciseState(pageTransition.getFrom(), user, criteria.getExerciseAffected());
					allowed = exerciseCriteriaSatisfied(exerciseState, criteria.getExerciseCriteria());

					if (!allowed) break;
				}

				if (criteria.isForPage()) {
					// TODO
				}
			}
		}


		return allowed;
	}

	/**
	 * Checks if exercise satisfies criteria
	 *
	 * @param exerciseState
	 * @param criteria
	 * @return satisfies
	 */
	private boolean exerciseCriteriaSatisfied(ExerciseState exerciseState, ExerciseCriteriaType criteria) {
		boolean satisfies = false;

		switch (criteria) {
			case noInput:
				satisfies = exerciseState.getInputState() == null || exerciseState.getInputState().isEmpty();
				break;
			case correctInput:
				satisfies = exerciseState.getInputState() != null && componentService.isExerciseCorrect(exerciseState);
				break;
			case incorrectInput:
				satisfies = exerciseState.getInputState() != null && !componentService.isExerciseCorrect(exerciseState);
				break;
		}

		return satisfies;
	}
}
