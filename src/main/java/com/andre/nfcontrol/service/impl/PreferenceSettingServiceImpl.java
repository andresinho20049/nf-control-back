package com.andre.nfcontrol.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.PreferenceSetting;
import com.andre.nfcontrol.repository.PreferenceSettingRepository;
import com.andre.nfcontrol.service.PreferenceSettingService;
import com.andre.nfcontrol.utils.SecurityContext;

@Service
public class PreferenceSettingServiceImpl implements PreferenceSettingService {

	@Autowired
	private PreferenceSettingRepository preferenceSettingRepository;

	// I can create a ConstraintValidator implementation if the team prefers
	private void validPersist(PreferenceSetting setting) {

		if (setting == null)
			throw new ProjectException("Category is null");

		if (setting.getId() == null) {

			if (setting.getUser() == null || setting.getUser().getId() == null)
				throw new ProjectException("User is required");

			if (preferenceSettingRepository.existsByUser_Id(setting.getUser().getId()))
				throw new ProjectException("A preference setting already exists for this user");
		}

		if (setting.getMaxLimit() == null)
			throw new ProjectException("Max limit is required");

		if (setting.getIsSendMail() == null)
			setting.setIsSendMail(false);

		if (setting.getIsSendSms() == null)
			setting.setIsSendSms(false);

	}

	@Override
	public void save(PreferenceSetting setting) {

		this.validPersist(setting);

		preferenceSettingRepository.save(setting);

	}

	@Override
	public List<PreferenceSetting> findAll() {

		return preferenceSettingRepository.findAll();
	}

	@Override
	public void update(PreferenceSetting setting) {

		this.validPersist(setting);

		if (setting.getId() == null)
			throw new ProjectException("Need to inform ID for update");

		preferenceSettingRepository.save(setting);

	}

	@Override
	public void delete(Long id) {
		this.findById(id);
		
		preferenceSettingRepository.deleteById(id);
	}

	@Override
	public PreferenceSetting findById(Long id) {

		return preferenceSettingRepository.findById(id)
				.orElseThrow(() -> new ProjectException("Preference Setting	 id: " + id + " not found"));
	}

	@Override
	public PreferenceSetting findByUserLogged() {
		
		Long userId = SecurityContext.getUserLogged().getId();
		
		return preferenceSettingRepository.findByUser_Id(userId)
					.orElseThrow(() -> new ProjectException("Preference Setting userId: " + userId + " not found"));
	}

}
