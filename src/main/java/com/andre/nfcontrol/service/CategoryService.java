package com.andre.nfcontrol.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.andre.nfcontrol.models.Category;

public interface CategoryService {
	
	void save(Category category);					//C
	
	List<Category> findAll(Boolean isArchive);		//R
	
	void update(Category category);					//U
	
	void delete(Long id);							//D
	
	void archive(Long id);
	
	Category findById(Long id);
	
	Page<Category> findByPage(Boolean isArchive, Integer page, Integer size, String order, String direction);
	
}
