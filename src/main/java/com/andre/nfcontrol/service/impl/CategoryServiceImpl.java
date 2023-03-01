package com.andre.nfcontrol.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.Category;
import com.andre.nfcontrol.repository.CategoryRepository;
import com.andre.nfcontrol.service.CategoryService;
import com.andre.nfcontrol.utils.Pagination;
import com.andre.nfcontrol.utils.SecurityContext;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	//I can create a ConstraintValidator implementation if the team prefers
	private void validPersist(Category category) {
		
		if(category == null)
			throw new ProjectException("Category is null");
		
		if(category.getName() == null || category.getName().isEmpty())
			throw new ProjectException("Name is required");
		
		if(category.getIsArchive() == null)
			category.setIsArchive(false);
		
	}

	@Override
	public void save(Category category) {
		
		this.validPersist(category);
		
		category.setUser(SecurityContext.getUserLogged());
		
		categoryRepository.save(category);
		
	}

	@Override
	public List<Category> findAll(Boolean isArchive) {
		
		Long userId = SecurityContext.getUserLogged().getId();
		
		if(isArchive != null)
			return categoryRepository.findByIsArchiveAndUser_Id(isArchive, userId);
		
		return categoryRepository.findByUser_Id(userId);
	}

	@Override
	public void update(Category category) {
		
		this.validPersist(category);
		
		if(category.getId() == null)
			throw new ProjectException("Need to inform ID for update");
		
		categoryRepository.save(category);
		
	}

	@Override
	public void delete(Long id) {
		this.findById(id);
		
		try {
			
			categoryRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new ProjectException("Category has relationship with some expense", e);
		}
		
	}

	@Override
	public void archive(Long id) {
		
		Category category = this.findById(id);
		category.setIsArchive(!category.getIsArchive());
		
		categoryRepository.save(category);
	}

	@Override
	public Category findById(Long id) {

		return categoryRepository.findById(id)
					.orElseThrow(() -> new ProjectException("Category id: " + id + " not found"));
	}

	@Override
	public Page<Category> findByPage(Boolean isArchive, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);

		Long userId = SecurityContext.getUserLogged().getId();
		
		if(isArchive != null)
			return categoryRepository.findByIsArchiveAndUser_Id(isArchive, userId, pageRequest);
		
		return categoryRepository.findByUser_Id(userId, pageRequest);
	}

}
