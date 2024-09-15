package com.box.bookstore.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ConfirmationToken {

	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private int tokenId;

    @Column(name="confirmation_token")
    private String confirmationToken;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;
    
    private String userEmail;
    
    private String userGmail;
    
}
