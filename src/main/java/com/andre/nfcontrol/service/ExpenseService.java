package com.andre.nfcontrol.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.andre.nfcontrol.models.Expense;

public interface ExpenseService {
	
	void save(Expense expense);				//C
	
	List<Expense> findAll(Integer year);	//R
	
	void update(Expense expense);			//U
	
	void delete(Long id);					//D
	
	Expense findById(Long id);
	
	Page<Expense> findByPage(Integer year, Integer page, Integer size, String order, String direction);
	
}
