package com.box.bookstore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.box.bookstore.model.ConfirmationToken;

public interface ConformationToken_repo extends JpaRepository<ConfirmationToken, Integer> {

}
