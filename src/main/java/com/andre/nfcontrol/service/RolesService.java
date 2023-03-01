package com.andre.nfcontrol.service;

import java.util.List;

import com.andre.nfcontrol.models.Roles;

public interface RolesService {
	
	void save(Roles roles); 	//C
	
	List<Roles> findAll();		//R
	
	void update(Roles roles);	//U
	
	void delete(Roles roles);	//D
	
	Roles findById(Long id);
	
	Roles findByName(String nameRole);

}
