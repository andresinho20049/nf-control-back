package com.andre.nfcontrol.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.andre.nfcontrol.models.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	List<Invoice> findByUser_Id(Long userId);
	
	Page<Invoice> findByUser_Id(Long userId, Pageable pageable);


	@Query("select e from Invoice e where year(e.accrualDate) = ?1 and e.user.id = ?2")
	List<Invoice> findByYearAndUser(Integer year, Long userId);

	@Query("select e from Invoice e where year(e.accrualDate) = ?1 and e.user.id = ?2")
	Page<Invoice> findByYearAndUser(Integer year, Long userId, Pageable pageable);
}
