package ch.uzh.marugoto.core.data.repository;

import com.arangodb.springframework.repository.ArangoRepository;

import ch.uzh.marugoto.core.data.entity.state.NotebookContent;

public interface NotebookContentRepository extends ArangoRepository<NotebookContent> {
}
