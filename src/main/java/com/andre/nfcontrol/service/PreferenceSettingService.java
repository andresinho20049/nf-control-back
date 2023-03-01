package com.andre.nfcontrol.service;

import java.util.List;

import com.andre.nfcontrol.models.PreferenceSetting;

public interface PreferenceSettingService {
	
	void save(PreferenceSetting preferenceSetting);			//C
	
	List<PreferenceSetting> findAll();						//R
	
	void update(PreferenceSetting preferenceSetting);		//U
	
	void delete(Long id);									//D
	
	PreferenceSetting findById(Long id);
	
	PreferenceSetting findByUserLogged();
	
}
