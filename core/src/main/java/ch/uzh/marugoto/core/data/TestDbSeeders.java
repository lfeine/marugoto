package ch.uzh.marugoto.core.data;


import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.marugoto.core.data.entity.Chapter;
import ch.uzh.marugoto.core.data.entity.ExerciseState;
import ch.uzh.marugoto.core.data.entity.Money;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.PageState;
import ch.uzh.marugoto.core.data.entity.PageTransition;
import ch.uzh.marugoto.core.data.entity.PageTransitionState;
import ch.uzh.marugoto.core.data.entity.Salutation;
import ch.uzh.marugoto.core.data.entity.Storyline;
import ch.uzh.marugoto.core.data.entity.StorylineState;
import ch.uzh.marugoto.core.data.entity.TextComponent;
import ch.uzh.marugoto.core.data.entity.TextExercise;
import ch.uzh.marugoto.core.data.entity.TextSolution;
import ch.uzh.marugoto.core.data.entity.User;
import ch.uzh.marugoto.core.data.entity.UserType;
import ch.uzh.marugoto.core.data.entity.VirtualTime;
import ch.uzh.marugoto.core.data.repository.ChapterRepository;
import ch.uzh.marugoto.core.data.repository.ComponentRepository;
import ch.uzh.marugoto.core.data.repository.ExerciseStateRepository;
import ch.uzh.marugoto.core.data.repository.PageRepository;
import ch.uzh.marugoto.core.data.repository.PageStateRepository;
import ch.uzh.marugoto.core.data.repository.PageTransitionRepository;
import ch.uzh.marugoto.core.data.repository.PageTransitionStateRepository;
import ch.uzh.marugoto.core.data.repository.StorylineRepository;
import ch.uzh.marugoto.core.data.repository.StorylineStateRepository;
import ch.uzh.marugoto.core.data.repository.UserRepository;


@Service
public class TestDbSeeders {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ChapterRepository chapterRepository;
	
	@Autowired
	private StorylineRepository storylineRepository;
	
	@Autowired
	private StorylineStateRepository storylineStateRepository;
	
	@Autowired
	private PageRepository pageRepository;
	
	@Autowired
	private PageTransitionRepository pageTransitionRepository;

	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired
	private PageStateRepository pageStateRepository;
	
	@Autowired
	private PageTransitionStateRepository pageTransitionStateRepository;
	
	@Autowired
	private ExerciseStateRepository exerciseStateRepository;
	

	public void createData() {
		var testUser1 = userRepository.save(new User(UserType.Guest, Salutation.Mr, "Fredi", "Kruger", "unittest@marugoto.ch", "test"));
		
		var testChapter1 = chapterRepository.save(new Chapter("Chapter-1", "icon-chapter-1"));
		var testChapter2 = chapterRepository.save(new Chapter("Chapter-2", "icon-chapter-2"));
		
		var testStoryline1 = storylineRepository.save(new Storyline("Storyline-1","icon-storyline-1",Duration.ofMinutes(10),true)); 
		
		var testPage1 = new Page("Page 1", true, null, testStoryline1);
		var testPage2 = new Page("Page 2", true, testChapter1, testStoryline1, false, Duration.ofMinutes(30), true, false, false, false);
		var testPage3 = new Page("Page 3", true, testChapter2, testStoryline1); 
		var testPage4 = new Page("Page 4", true, testChapter1, testStoryline1);
		
		var testComponent1 = componentRepository
				.save(new TextComponent(0, 300, 200, 200, "Some example title", "Some example text for component"));

		var testExercise1 = new TextExercise(100, 100, 400, 400, 5, 25, "Wording", "What does 'domo arigato' mean?", null, 20);
		testExercise1.addTextSolution(new TextSolution("Thank you"));	
		testExercise1.addTextSolution(new TextSolution("Thank's"));
		componentRepository.save(testExercise1);
		
		testPage1.addComponent(testComponent1);
		testPage2.addComponent(testExercise1);
		testPage4.setTime(new VirtualTime(Duration.ofDays(7), false));
		testPage4.setMoney(new Money(1000, false));
		pageRepository.save(testPage1);
		pageRepository.save(testPage2);
		pageRepository.save(testPage3);
		pageRepository.save(testPage4);
		
		var testPageTransition1to2 = new PageTransition(testPage1, testPage2, "confirm");
		var testPageTransition1to3 = new PageTransition(testPage1, testPage3, "submit");
		var testPageTransition2to4 = new PageTransition(testPage2, testPage4, "login");
		pageTransitionRepository.save(testPageTransition1to2);
		pageTransitionRepository.save(testPageTransition1to3);
		pageTransitionRepository.save(testPageTransition2to4);
		
		
		// States
		
		var testStorylineState1 = new StorylineState(LocalDateTime.now(),testStoryline1,testUser1);
		var testPageState1 = new PageState(testPage1,testUser1);
		var testPageState2 = new PageState(testPage2,testUser1);
		storylineStateRepository.save(testStorylineState1);
		
		pageStateRepository.save(testPageState1);
		pageStateRepository.save(testPageState2);
		
		exerciseStateRepository.save(new ExerciseState(testExercise1));
		
		pageTransitionStateRepository.save(new PageTransitionState(true, testPageTransition1to2, testUser1));
		pageTransitionStateRepository.save(new PageTransitionState(false, testPageTransition1to3, testUser1));
		pageTransitionStateRepository.save(new PageTransitionState(true, testPageTransition2to4, testUser1));
	}
}
