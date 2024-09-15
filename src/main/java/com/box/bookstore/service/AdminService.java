package com.box.bookstore.service;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;

import com.box.bookstore.model.AdminModel;

public interface AdminService {
	
	AdminModel findAdmin(AdminModel adminModel);
	
	void changeEmailPassword(AdminModel adminModel);
	
	void save(AdminModel adminModel);

}
