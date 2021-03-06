package com.cde.fse.security.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cde.fse.model.User;
import com.cde.fse.model.Users;
import com.cde.fse.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

private final static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Inside loadUserByUsername method start");
		
		Users user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		
		return new org.springframework.security.core.userdetails.User(user.getRole(), user.getPassword(), new ArrayList<>());
	}
	
	public Users getUserRole(String username) {
		log.info("Inside getUserRole method start");
		Users role = userRepository.getRoleOfUser(username);
		log.info("Role in getUserRole returning ::::::::::::" + role);
		return role;
	}
}
