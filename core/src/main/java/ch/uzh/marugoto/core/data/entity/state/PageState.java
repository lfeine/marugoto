package ch.uzh.marugoto.core.data.entity.state;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Ref;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.marugoto.core.data.entity.topic.NotebookEntry;
import ch.uzh.marugoto.core.data.entity.topic.Page;

/**
 *  Page state - should contain information related to page state for user 
 */
@Document
@JsonIgnoreProperties({"page", "gameState"})
public class PageState {
	@Id
	private String id;
	private LocalDateTime enteredAt;
	private LocalDateTime leftAt;
	private List<PageTransitionState> pageTransitionStates;
	private List<String> notebookEntries;
	@Ref
	private GameState gameState;
	@Ref
	private Page page;

	public PageState() {
		super();
	}

	public PageState(Page page) {
		super();
		this.page = page;
		this.enteredAt = LocalDateTime.now();
		this.pageTransitionStates = new ArrayList<>();
		this.notebookEntries = new ArrayList<>();
	}

	public PageState(Page page, GameState gameState) {
		this(page);
		this.gameState = gameState;
	}

	public String getId() {
		return id;
	}

	public LocalDateTime getEnteredAt() {
		return enteredAt;
	}

	public void setEnteredAt(LocalDateTime enteredAt) {
		this.enteredAt = enteredAt;
	}

	public LocalDateTime getLeftAt() {
		return leftAt;
	}

	public void setLeftAt(LocalDateTime leftAt) {
		this.leftAt = leftAt;
	}

	public List<PageTransitionState> getPageTransitionStates() {
		return pageTransitionStates;
	}

	public void addPageTransitionState(PageTransitionState pageTransitionState) {
		this.pageTransitionStates.add(pageTransitionState);
	}

	public void setPageTransitionStates(List<PageTransitionState> pageTransitionStates) {
		this.pageTransitionStates = pageTransitionStates;
	}

	public List<String> getNotebookEntries() {
		return notebookEntries;
	}

	public void addNotebookEntry(NotebookEntry notebookEntry) {
		this.notebookEntries.add(notebookEntry.getId());
	}

	public void setNotebookEntries(List<String> notebookEntries) {
		this.notebookEntries = notebookEntries;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
