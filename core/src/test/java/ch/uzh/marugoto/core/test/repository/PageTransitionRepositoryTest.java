package ch.uzh.marugoto.core.test.repository;

import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.marugoto.core.data.entity.Chapter;
import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.PageTransition;
import ch.uzh.marugoto.core.data.repository.ChapterRepository;
import ch.uzh.marugoto.core.data.repository.PageRepository;
import ch.uzh.marugoto.core.data.repository.PageTransitionRepository;
import ch.uzh.marugoto.core.test.BaseCoreTest;

public class PageTransitionRepositoryTest extends BaseCoreTest{

	@Autowired
	private PageRepository pageRepository;
	
	@Autowired
	private ChapterRepository chapterRepository;
	
	@Autowired
	private PageTransitionRepository pageTransitionRepository;
	
	@Test
	public void testCreatePageTransition() {
		var chapter = chapterRepository.save(new Chapter("Chapter 1", "icon_chapter_1"));
		var page1 = new Page("Page 1", true, null);
		var page2 = new Page("Page 2", true, chapter, false, Duration.ofMinutes(30), true, false, false, false);

		pageRepository.save(page1);
		pageRepository.save(page2);
		PageTransition pageTransition = pageTransitionRepository.save(new PageTransition(page1, page2, "confirm"));
		assertNotNull(pageTransition);
		assertEquals("confirm", pageTransition.getButtonText());
	}
}