package com.javainuse.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JWTAuthentificationFilter extends UsernamePasswordAuthenticationFilter
{

	private final AuthenticationManager authenticationManager;

	public JWTAuthentificationFilter(final AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
			throws AuthenticationException
	{
		com.javainuse.model.AppUser appUser = null;
		try
		{
			appUser = new ObjectMapper().readValue(request.getInputStream(), com.javainuse.model.AppUser.class);
			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain, final Authentication authResult) throws IOException, ServletException
	{


		final User user = (User) authResult.getPrincipal();


		final List<String> roles = new ArrayList<>();
		authResult.getAuthorities().forEach(a -> {
			roles.add(a.getAuthority());
		});

		final String jwt = JWT.create().withIssuer(request.getRequestURI()).withSubject(user.getUsername())
				.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 24 * 3600))
				.sign(Algorithm.HMAC256("dris.elazzouzi@gmail.com"));

		response.addHeader("accessToken", jwt);
	}



}
