package ch.uzh.marugoto.backend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.marugoto.core.data.entity.Page;
import ch.uzh.marugoto.core.data.entity.TransitionChosenOptions;
import ch.uzh.marugoto.core.data.entity.User;
import ch.uzh.marugoto.core.exception.PageTransitionNotAllowedException;
import ch.uzh.marugoto.core.exception.TopicNotSelectedException;
import ch.uzh.marugoto.core.exception.UserStatesNotInitializedException;
import ch.uzh.marugoto.core.service.StateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

/**
 * API to get the page with pageTransitions.
 */
@RestController
public class PageController extends BaseController {
	
	@Autowired
	private StateService stateService;

	/**
	 * Loads last visited page for user
	 * If it's first time for user then it should start chosen topic
	 *
	 * @return
	 * @throws AuthenticationException
	 */
	@ApiOperation(value = "Load current page.", authorizations = { @Authorization(value = "apiKey") })
	@GetMapping("pages/current")
	public HashMap<String, Object> getPage() throws AuthenticationException, TopicNotSelectedException {
		try {
			User authenticatedUser = getAuthenticatedUser();
			var response = stateService.getStates(authenticatedUser);
			response.put("page", authenticatedUser.getCurrentPageState().getPage());
			return response;
		} catch (UserStatesNotInitializedException e) {
			throw new TopicNotSelectedException(messages.get("topicNotSelected"));
		}
	}

	/**
	 * Page transition from page to page
	 * Everything that should happen before loading page and leaving previous one
	 *
	 * @param pageTransitionId
	 * @param chosenByPlayer
	 * @return
	 * @throws AuthenticationException
	 * @throws PageTransitionNotAllowedException
	 */
	@ApiOperation(value = "Handles a pagetransition from the current page to another page.", authorizations = { @Authorization(value = "apiKey") })
	@RequestMapping(value = "pageTransitions/doPageTransition/pageTransition/{pageTransitionId}", method = RequestMethod.POST)
	public Map<String, Object> doPageTransition(@ApiParam("ID of page updateStatesAfterTransition") @PathVariable String pageTransitionId,
			@ApiParam("Is chosen by player ") @RequestParam("chosenByPlayer") boolean chosenByPlayer) throws AuthenticationException, PageTransitionNotAllowedException, TopicNotSelectedException {
		User user = getAuthenticatedUser();
		TransitionChosenOptions chosenBy = chosenByPlayer ? TransitionChosenOptions.player : TransitionChosenOptions.autoTransition;
		Page nextPage = stateService.doPageTransition(chosenBy, "pageTransition/" + pageTransitionId, user);

		try {
			var response = stateService.getStates(user);
			response.put("page", nextPage);
			return response;
		} catch (UserStatesNotInitializedException e) {
			throw new TopicNotSelectedException(messages.get("topicNotSelected"));
		}
	}
}
