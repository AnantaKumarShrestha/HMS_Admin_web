package com.box.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
public class AdminModel {
	
	@Id
	private int id;
	@Email(message = "Invalid email format")
	private String email;
	private String password;
	private String gmail;
	private String changedpassword;
	@Transient
	private String cpassword;

}
