package com.javainuse.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUser
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	private String firstName;

	private String lastName;

	private Date birthDate;

	private String city;

	private String country;

	private String avatar;

	private String company;

	private String jobPosition;

	private String mobile;

	private String email;

	private String password;

	private String confirmedPassword;

	private boolean actived;

	@ManyToMany(fetch = FetchType.EAGER)
	private final Collection<AppRole> roles = new ArrayList<>();

}

