package com.projet.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projet.stock.model.Admin;
import com.projet.stock.model.Expert;


	@Repository
	public interface AdminRepository extends JpaRepository<Admin, Long>{
		
		Optional<Admin> findByUsername(String username);

		Boolean existsByUsername(String username);

		Boolean existsByEmail(String email);
		
		Optional<Admin> findByImage(String image);

	}


