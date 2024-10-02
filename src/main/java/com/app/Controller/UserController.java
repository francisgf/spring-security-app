package com.app.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users") 
@RequiredArgsConstructor
public class UserController {

	@GetMapping("")
	public String getUsers() {
		return "user list";
	}

}
