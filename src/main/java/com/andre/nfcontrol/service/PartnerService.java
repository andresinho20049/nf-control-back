package com.andre.nfcontrol.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.andre.nfcontrol.models.Partner;

public interface PartnerService {
	
	void save(Partner partner);			//C
	
	List<Partner> findAll();			//R
	
	void update(Partner partner);		//U
	
	void delete(Long id);				//D
	
	Partner findById(Long id);
	
	Page<Partner> findByPage(String search, Integer page, Integer size, String order, String direction);
	
}
