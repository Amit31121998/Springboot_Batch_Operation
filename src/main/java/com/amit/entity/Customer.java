package com.amit.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Customer {

	@Id
	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private String gender;

	private String contactNum;

	private String country;

	private String dob;
}
