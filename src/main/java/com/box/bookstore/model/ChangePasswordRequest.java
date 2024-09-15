package com.box.bookstore.model;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	
	private String email;
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;

}
