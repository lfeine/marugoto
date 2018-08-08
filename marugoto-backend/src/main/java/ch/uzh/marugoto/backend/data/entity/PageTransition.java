package ch.uzh.marugoto.backend.data.entity;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge
public class PageTransition {

	@Id
	private String id;

	@From
	private Page from;

	@To
	private Page to;

	private String buttonText;

	public String getId() {
		return id;
	}

	public Page getFrom() {
		return from;
	}

	public Page getTo() {
		return to;
	}
	
	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public PageTransition() {
		super();
	}

	public PageTransition(Page from, Page to, String buttonText) {
		super();
		this.from = from;
		this.to = to;
		this.buttonText = buttonText;
	}
}