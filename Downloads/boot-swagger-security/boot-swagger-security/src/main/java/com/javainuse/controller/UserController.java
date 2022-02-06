package com.javainuse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;
import com.javainuse.configuration.JwtTokenUtil;
import com.javainuse.model.AppUser;
import com.javainuse.model.JwtRequest;
import com.javainuse.model.JwtResponse;
import com.javainuse.service.AppUserService;
import com.javainuse.service.FileStorageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.bytebuddy.utility.RandomString;


@RestController
@SecurityRequirement(name = "tangermed")
public class UserController
{

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final List<AppUser> users = new ArrayList<>();

	@RequestMapping(value = "/api/users/generate", method = RequestMethod.GET, produces = "application/json")
	public List<AppUser> generate(@RequestParam("count") final int count)
	{

		final Faker faker = new Faker();
		for (int i = 0; i < count; i++)
		{
			final AppUser appUser = new AppUser();
			appUser.setFirstName(faker.name().firstName());
			appUser.setLastName(faker.name().lastName());
			appUser.setBirthDate(faker.date().birthday());
			appUser.setCity(faker.address().cityName());
			appUser.setCountry(faker.address().country());
			appUser.setAvatar(faker.avatar().image());
			appUser.setCompany(faker.company().name());
			appUser.setJobPosition(faker.job().position());
			appUser.setMobile(faker.phoneNumber().phoneNumber());
			appUser.setUsername(faker.name().username());
			appUser.setEmail(faker.name().username() + "@gmail.com");
			appUser.setPassword(RandomString.make(10));
			System.out.println(appUser.getUsername());
			users.add(appUser);
		}
		return users;
	}


	@RequestMapping(value = "/api/users/batch", method = RequestMethod.POST)
	public void usersBatch()
	{
		for (final AppUser appUser : users)
		{
			appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
			appUserService.saveUser(appUser);
			appUserService.addRoleToUser(appUser.getUsername(), "USER");
		}

	}

	@RequestMapping(value = "/api/auth", method = RequestMethod.POST)
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody final JwtRequest authenticationRequest) throws Exception
	{

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(final String username, final String password) throws Exception
	{
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}
		catch (final DisabledException e)
		{
			throw new Exception("USER_DISABLED", e);
		}
		catch (final BadCredentialsException e)
		{
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@GetMapping("/api/users/me")
	public UserDetails consulterMonPofil()
	{

		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());

		return userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	@GetMapping("api/users/{username}")
	public UserDetails consulterProfil(@PathVariable("username") final String username)
	{


		return userDetailsService.loadUserByUsername(username);
	}
}

