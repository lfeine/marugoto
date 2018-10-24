package ch.uzh.marugoto.core.data.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

import ch.uzh.marugoto.core.data.entity.PageTransition;

public interface PageTransitionRepository extends ArangoRepository<PageTransition> {
	
	@Query("FOR page, pageTransition IN OUTBOUND @fromPageId pageTransition RETURN pageTransition")
	List<PageTransition> findByPageId(@Param("fromPageId") String fromPageId);

	@Query("FOR page, pageTransition IN OUTBOUND @pageId pageTransition " +
			"FOR criteria IN pageTransition.criteria FIlTER criteria.affectedExercise == @exerciseId " +
			"RETURN pageTransition"
	)
    PageTransition findByPageAndExercise(@Param("pageId") String pageId, @Param("exerciseId") String exerciseId);
}