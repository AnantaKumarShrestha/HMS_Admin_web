package com.box.bookstore.model;

import com.box.bookstore.encryption.EncryptorDecryptor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class DoctorModel {
    
	@Id
	@GeneratedValue
	private int id;
	private String email;
	@Convert(converter = EncryptorDecryptor.class)
	private String password;
	private String registered;
	private String authorized;
	@Transient
	private String conpassword;
	

	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="doctor_personal_details_Id")
	private DoctorPersonalDetailsModel doctorPersonalDetailsModel;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="doctor_study_details_Id")
	private DoctorStudyDetailsModel doctorStudyDetailsModel;
	
	
}
