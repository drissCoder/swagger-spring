package com.javainuse.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/swagger-ui/**", "/uploadFile/**", "/h2-console/**", "/tangermed-openapi/**")
				.permitAll().anyRequest().authenticated().and().httpBasic();
		http.addFilter(new JWTAuthentificationFilter(authenticationManager()));
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
		//auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder.encode("1234")).authorities("ADMIN");

	}

	/*
	 * @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
	 */

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}
}
