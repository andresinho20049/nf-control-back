package com.andre.nfcontrol.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.andre.nfcontrol.models.Invoice;

public interface InvoiceService {
	
	void save(Invoice invoice);				//C
	
	List<Invoice> findAll(Integer year);		//R
	
	void update(Invoice invoice);			//U
	
	void delete(Long id);						//D
	
	Invoice findById(Long id);
	
	Page<Invoice> findByPage(Integer year, Integer page, Integer size, String order, String direction);
	
}
