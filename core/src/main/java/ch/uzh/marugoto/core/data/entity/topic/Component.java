package ch.uzh.marugoto.core.data.entity.topic;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Ref;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;

/**
 * 
 * Base Component entity
 *
 */
@Document("component")
@JsonIgnoreProperties({ "id", "page" })
abstract public class Component {
	@Id
	private String id;
	private int numberOfColumns;
	private int offsetColumns = 0;
	private int renderOrder = 1;
	private boolean showInNotebook;
	private NotebookContentCreateAt showInNotebookAt;
	@Ref
	private Page page;

	public Component() {
		super();
	}

	public Component(int numberOfColumns) {
		this();
		this.numberOfColumns = numberOfColumns;
	}

	public Component(int numberOfColumns, Page page) {
		this(numberOfColumns);
		this.page = page;
	}

	public String getId() {
		return id;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public int getRenderOrder() {
		return renderOrder;
	}

	public void setRenderOrder(int renderOrder) {
		this.renderOrder = renderOrder;
	}

	public boolean isShownInNotebook() {
		return showInNotebook;
	}

	public void setShowInNotebook(boolean showInNotebook) {
		this.showInNotebook = showInNotebook;
	}

	public NotebookContentCreateAt getShowInNotebookAt() {
		return showInNotebookAt;
	}

	public void setShowInNotebookAt(NotebookContentCreateAt showInNotebookAt) {
		this.showInNotebookAt = showInNotebookAt;
	}

	public int getOffsetColumns() {
		return offsetColumns;
	}

	public void setOffsetColumns(int offsetColumns) {
		this.offsetColumns = offsetColumns;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public boolean equals(Object o) {
		boolean equals = false;

		if (o instanceof Component) {
			Component component = (Component) o;
			equals = id.equals(component.id);
		}

		return equals;
	}
}
