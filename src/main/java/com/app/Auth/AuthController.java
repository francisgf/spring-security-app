package com.app.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth") 
@RequiredArgsConstructor
public class AuthController {


	private final AuthService authService;
	
	@PostMapping( value = "login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
	    
		System.out.println("login ------------------->");
		AuthResponse authResponse = authService.login(request);
	    return ResponseEntity.ok(authResponse); 
	}
	
	
	@PostMapping( value = "register")
	public ResponseEntity<AuthResponse>register(@RequestBody LoginRequest request) {
	    
		System.out.println("register ------------------->");
		AuthResponse authResponse = authService.login(request);
		 return ResponseEntity.ok(authResponse); 	}
	
}

