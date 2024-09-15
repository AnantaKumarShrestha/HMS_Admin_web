package com.box.bookstore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.box.bookstore.model.AdminModel;

public interface Admin_repo extends JpaRepository<AdminModel, Integer>{

	AdminModel findByEmailAndPassword(String email,String password);

//	AdminModel findByEmail(String email);
//	
	@Query("SELECT a FROM AdminModel a WHERE a.email = :email")
    AdminModel getByMail(@Param("email") String email);
	
}
