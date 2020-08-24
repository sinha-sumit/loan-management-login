package com.cde.fse.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
@EnableHystrix
public class TestController {
	
	@Autowired
    RestTemplate restTemplate;
	
	/*
	 * @RequestMapping(value = "/")
	 * 
	 * @HystrixCommand(fallbackMethod = "fallback_hello", commandProperties = {
	 * 
	 * @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
	 * value = "1000") })
	 */
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}


	@GetMapping("/admin")
	//@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
	@HystrixCommand(fallbackMethod = "callLoanManagementLoginApi_Fallback")
    public String callLoanManagementLoginApi(String username) {
 
        System.out.println("Getting Login details for " + username);
 
        String response = restTemplate
                .exchange("http://localhost:8091/api/test/all"
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<String>() {
            }, username).getBody();
 
        System.out.println("Response Received as " + response + " -  " + new Date());
 
        return "NORMAL FLOW !!! - User Name -  " + username + " :::  " +
                    " Login Details " + response + " -  " + new Date();
    }
}
