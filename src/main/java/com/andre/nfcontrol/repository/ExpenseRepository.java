package com.andre.nfcontrol.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.andre.nfcontrol.models.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	List<Expense> findByUser_Id(Long userId);
	
	Page<Expense> findByUser_Id(Long userId, Pageable pageable);
	
	
	@Query("select e from Expense e where year(e.accrualDate) = ?1 and e.user.id = ?2")
	List<Expense> findByYearAndUser(Integer year, Long userId);
	
	@Query("select e from Expense e where year(e.accrualDate) = ?1 and e.user.id = ?2")
	Page<Expense> findByYearAndUser(Integer year, Long userId, Pageable pageable);
	
}
