package ch.uzh.marugoto.core.test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.marugoto.core.data.entity.topic.Chapter;
import ch.uzh.marugoto.core.data.repository.ChapterRepository;
import ch.uzh.marugoto.core.test.BaseCoreTest;

/**
 * Simple test cases for ChapterRepository.
 */
public class ChapterRepositoryTest extends BaseCoreTest{

	@Autowired
	private ChapterRepository chapterRepository;
	
	@Test
	public void testCreateChapter() {
		Chapter chapter = chapterRepository.save(new Chapter("ChapterRepository 1"));
		assertNotNull(chapter);
		assertEquals("ChapterRepository 1", chapter.getTitle());
	}

}
