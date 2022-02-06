package com.javainuse.service;

import java.util.List;

import com.javainuse.model.AppRole;
import com.javainuse.model.AppUser;


public interface AppUserService
{

	public AppUser saveUser(String username, String password, String confirmedPassword);

	public AppUser saveUser(AppUser appUser);

	public AppRole save(AppRole role);

	public AppUser loadUserByUsername(String username);

	public void addRoleToUser(String username, String roleName);

	public List<AppUser> getAllUsers();

	public AppUser deleteUser(Long id);
}
