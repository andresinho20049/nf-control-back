package com.andre.nfcontrol.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andre.nfcontrol.models.PreferenceSetting;

@Repository
public interface PreferenceSettingRepository extends JpaRepository<PreferenceSetting, Long> {
	
	Boolean existsByUser_Id(Long userId);
	
	Optional<PreferenceSetting> findByUser_Id(Long userId);
	
}
