package com.andre.nfcontrol.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andre.nfcontrol.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category> findByUser_Id(Long userId);
	
	Page<Category> findByUser_Id(Long userId, Pageable pageable);
	
	
	List<Category> findByIsArchiveAndUser_Id(Boolean isArchive, Long userId);
	
	Page<Category> findByIsArchiveAndUser_Id(Boolean isArchive, Long userId, Pageable pageable);
	
	
	Page<Category> findByNameIgnoreCaseStartingWithAndUser_Id(String search, Long userId, Pageable pageable);
	
	Page<Category> findByIsArchiveAndNameIgnoreCaseStartingWithAndUser_Id(Boolean isArchive, String search, Long userId, Pageable pageable);
}
