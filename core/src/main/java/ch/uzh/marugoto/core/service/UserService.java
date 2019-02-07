package ch.uzh.marugoto.core.service;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.uzh.marugoto.core.data.entity.StorylineState;
import ch.uzh.marugoto.core.data.entity.User;
import ch.uzh.marugoto.core.data.repository.UserRepository;
import ch.uzh.marugoto.core.exception.RequestValidationException;

/**
 * Service for handling user-related tasks like authentication, authorization
 * and registration/sign-up.
 */
@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User getUserByMail(String mail) {
		return userRepository.findByMail(mail);
	}
	
	public User findUserByResetToken(String resetToken) {
		return userRepository.findByResetToken(resetToken);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var applicationUser = this.getUserByMail(username);
		if (applicationUser == null)
			throw new UsernameNotFoundException(username);

		return new org.springframework.security.core.userdetails.User(applicationUser.getMail(),
				applicationUser.getPasswordHash(), Collections.emptyList());
	}

	public void updateLastLoginAt(User user) {
		user.setLastLoginAt(LocalDateTime.now());
		userRepository.save(user);
	}

	public void updateStorylineState(User user, StorylineState storylineState) {
		user.setCurrentStorylineState(storylineState);
		saveUser(user);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}	
}
