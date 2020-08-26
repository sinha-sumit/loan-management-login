package com.cde.fse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cde.fse.model.Users;
import com.cde.fse.payload.request.LoginRequest;
import com.cde.fse.payload.response.MessageResponse;
import com.cde.fse.repository.RoleRepository;
import com.cde.fse.repository.UserRepository;
import com.cde.fse.security.jwt.JwtUtils;
import com.cde.fse.security.service.UserDetailsServiceImpl;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/login/V1")
public class AuthController {

	private final static Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EurekaClient eurekaClient;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@HystrixCommand(fallbackMethod="loginFailure")
	public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest) throws Exception {
		log.info("Start createAuthenticationToken:: JwtAuthenticationController");
		
		Users role = userDetailsServiceImpl.getUserRole(authenticationRequest.getUsername());

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtUtils.generateJwtToken(userDetails.getUsername());
		
		return ResponseEntity.ok(new MessageResponse(token, role.getRole()));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS" + e + " user: " + username + " pwd: " + password);
		}
	}

	private ResponseEntity<?> loginFailure(@RequestBody LoginRequest authenticationRequest) {

		return ResponseEntity.ok(new MessageResponse("token", "role"));
	}
}
