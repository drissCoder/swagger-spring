package com.javainuse.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javainuse.model.AppRole;


public interface AppRoleRepository extends JpaRepository<AppRole, Long>
{

	public AppRole findByRoleName(String roleName);

}
