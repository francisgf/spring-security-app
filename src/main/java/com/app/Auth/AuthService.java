package com.app.Auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Entity.Role;
import com.app.Entity.User;
import com.app.Jwt.JwtService;
import com.app.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service

public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private  PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;

	

	
	 public AuthResponse login(LoginRequest request) {
	       
		 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
			UserDetails user = userRepository.findByUserName(request.getUserName()).orElseThrow();
	        String token=jwtService.getToken(user);
	        System.out.println("autenticando:" + token );
	        return AuthResponse.builder()
	            .token(token)
	            .build();

	    }
	/***
	 * user register
	 * 
	 * @param request
	 * @return AuthResponse
	 */
	public AuthResponse register(LoginRequest request) {

		  Optional<User> existingUser = userRepository.findByUserName(request.getUserName());
		    if (existingUser.isPresent()) {
		        throw new IllegalStateException("Username already exists");
		    }
		
		User user = User.builder().userName(request.getUserName())
				.password(passwordEncoder.encode(request.getPassword())).firstName(request.getFirstName())
				.lastName(request.getLastName()).country(request.getCountry()).role(Role.USER).build();

		userRepository.save(user);

		return AuthResponse.builder().token(jwtService.getToken(user)).build();
	}

}
