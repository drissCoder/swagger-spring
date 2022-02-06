package com.javainuse.configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javainuse.model.AppUser;
import com.javainuse.service.AppUserService;


@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

	@Autowired
	private AppUserService appUserService;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{

		final AppUser appUser = appUserService.loadUserByUsername(username);
		if (appUser == null)
		{
			throw new UsernameNotFoundException("invalid User");
		}
		final Collection<GrantedAuthority> authorities = new ArrayList<>();

		appUser.getRoles().forEach(r -> {
			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
		});
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);
	}

}
