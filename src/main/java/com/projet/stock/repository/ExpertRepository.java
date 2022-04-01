package com.projet.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.projet.stock.model.Expert;
import com.projet.stock.model.User;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long>{
	
	Optional<Expert> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	Optional<Expert> findByImage(String image);


	
}
