package com.example.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.core.AuthenticatedUser;
import com.example.core.UserDTO;
import com.example.core.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	private final UserService userService;

	@Autowired
	public UserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
		Objects.requireNonNull(token, "token must not be null");
		if (token.getPrincipal() == null) {
			throw new UsernameNotFoundException(Principal.class.getSimpleName() + " is null!");
		}

		String username = token.getPrincipal().toString().toUpperCase(Locale.ROOT);

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (token.getDetails() instanceof GrantedAuthoritiesContainer) {
			GrantedAuthoritiesContainer details = (GrantedAuthoritiesContainer) token.getDetails();
			for (GrantedAuthority jeeRole : details.getGrantedAuthorities()) {
				authorities.add(new SimpleGrantedAuthority(jeeRole.getAuthority().toUpperCase(Locale.ROOT)));
			}
		}
		Set<String> roles = decodeRoles(authorities);
		AuthenticatedUser user = new AuthenticatedUser(username, roles, authorities);

		token.setAuthenticated(true);

		log.info("Authenticated user: [{}].", user);

		UserDTO userDto = userService.findUserById(username);
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());

		return user;
	}

	private static Set<String> decodeRoles(Collection<? extends GrantedAuthority> authorities) {
		Set<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		if (!roles.isEmpty()) {
			return roles;
		}
		throw new AuthenticationException("Missing role") { };
	}

}
