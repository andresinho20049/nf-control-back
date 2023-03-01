package com.andre.nfcontrol.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.Expense;
import com.andre.nfcontrol.repository.ExpenseRepository;
import com.andre.nfcontrol.service.ExpenseService;
import com.andre.nfcontrol.utils.Pagination;
import com.andre.nfcontrol.utils.SecurityContext;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	// I can create a ConstraintValidator implementation if the team prefers
	private void validPersist(Expense expense) {

		if (expense == null)
			throw new ProjectException("Category is null");

		if (expense.getName() == null || expense.getName().isEmpty())
			throw new ProjectException("Name is required");

	}

	@Override
	public void save(Expense expense) {

		this.validPersist(expense);
		
		expense.setUser(SecurityContext.getUserLogged());

		expenseRepository.save(expense);

	}

	@Override
	public List<Expense> findAll(Integer year) {
		
		Long userId = SecurityContext.getUserLogged().getId();

		if(year != null)
			return expenseRepository.findByYearAndUser(year, userId);
		
		return expenseRepository.findByUser_Id(userId);
	}

	@Override
	public void update(Expense expense) {

		this.validPersist(expense);

		if (expense.getId() == null)
			throw new ProjectException("Need to inform ID for update");

		expenseRepository.save(expense);

	}

	@Override
	public void delete(Long id) {
		this.findById(id);
		
		expenseRepository.deleteById(id);
	}

	@Override
	public Expense findById(Long id) {

		return expenseRepository.findById(id)
				.orElseThrow(() -> new ProjectException("Expense id: " + id + " not found"));
	}

	@Override
	public Page<Expense> findByPage(Integer year, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);

		Long userId = SecurityContext.getUserLogged().getId();
		
		if(year != null)
			return expenseRepository.findByYearAndUser(year, userId, pageRequest);

		return expenseRepository.findByUser_Id(userId, pageRequest);
	}

}
