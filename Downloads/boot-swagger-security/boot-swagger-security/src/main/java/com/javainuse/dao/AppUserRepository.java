package com.javainuse.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javainuse.model.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, Long>
{

	public AppUser findByUsername(String username);

}
