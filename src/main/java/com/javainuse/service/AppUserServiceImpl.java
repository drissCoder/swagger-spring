package com.javainuse.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javainuse.dao.AppRoleRepository;
import com.javainuse.dao.AppUserRepository;
import com.javainuse.model.AppRole;
import com.javainuse.model.AppUser;


@Service
@Transactional
public class AppUserServiceImpl implements AppUserService
{


	private final AppUserRepository appUserRepository;

	private final AppRoleRepository appRoleRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public AppUserServiceImpl(final AppUserRepository appUserRepository, final AppRoleRepository appRoleRepository,
			final BCryptPasswordEncoder bCryptPasswordEncoder)
	{

		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public AppUser saveUser(final String username, final String password, final String confirmedPassword)
	{
		final AppUser user = appUserRepository.findByUsername(username);
		if (user != null)
		{
			throw new RuntimeException("User Already exists");
		}
		if (!password.equals(confirmedPassword))
		{
			throw new RuntimeException("Please confirm your password");
		}
		final AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setPassword(bCryptPasswordEncoder.encode(password));
		appUserRepository.save(appUser);
		addRoleToUser(username, "USER");

		return appUser;
	}

	@Override
	public AppRole save(final AppRole role)
	{

		return appRoleRepository.save(role);
	}

	@Override
	public AppUser loadUserByUsername(final String username)
	{
		// TODO Auto-generated method stub
		return appUserRepository.findByUsername(username);

	}

	@Override
	public void addRoleToUser(final String username, final String roleName)
	{
		final AppUser appUser = appUserRepository.findByUsername(username);
		final AppRole appRole = appRoleRepository.findByRoleName(roleName);
		appUser.getRoles().add(appRole);

	}

	@Override
	public List<AppUser> getAllUsers()
	{
		// TODO Auto-generated method stub
		return appUserRepository.findAll();
	}

	@Override
	public AppUser deleteUser(final Long id)
	{
		final AppUser user = appUserRepository.findById(id).get();
		// TODO Auto-generated method stub
		appUserRepository.delete(user);

		return user;
	}

	@Transactional
	@Override
	public AppUser saveUser(final AppUser appUser)
	{
		// TODO Auto-generated method stub
		return appUserRepository.saveAndFlush(appUser);
	}

}
