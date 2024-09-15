package com.box.bookstore.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.box.bookstore.model.DoctorModel;

public interface Doctor_repo extends JpaRepository<DoctorModel, Integer> {

	DoctorModel findByEmailAndPassword(String email,String password);

	DoctorModel findByEmail(String email);
	
	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM DoctorModel p WHERE p.doctorPersonalDetailsModel.gmail = :gmail")
    boolean existsByGmail(@Param("gmail") String gmail);
}
