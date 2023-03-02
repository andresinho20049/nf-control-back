package com.andre.nfcontrol.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.PreferenceSetting;
import com.andre.nfcontrol.models.User;
import com.andre.nfcontrol.repository.UserRepository;
import com.andre.nfcontrol.service.PreferenceSettingService;
import com.andre.nfcontrol.service.UserService;
import com.andre.nfcontrol.utils.Pagination;

@Service
public class UserServiceImpl implements UserService {
	
//	@Value("${directory.template.img.logo}")
//	private String imgLogo;
//	
//	@Value("${directory.template.temppassword}")
//	private String tempPasswordDir;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PreferenceSettingService preferenceSettingService;
	
//	@Autowired
//	private MailService mailService;

	@Override
	public void save(User user) {

		if (user == null)
			throw new ProjectException("User is null");

		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new ProjectException("E-mail is required");

		if (userRepository.existsByEmail(user.getEmail()))
			throw new ProjectException("Email is already registered");
		
		if(user.getPassword() == null || user.getPassword().isEmpty())
			throw new ProjectException("Password is required");
		
		String regex = "^(?=.*\\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(user.getPassword());
		if(!matcher.matches())
			throw new ProjectException("Weak password, please enter uppercase, lowercase, special characters, numbers and be at least 8 characters long");
			
		
		/*Send mail: 
		 * 
		 * Google removed Less secure apps, and not free server SMTP
		 * 
		String tempPasswordHTML = mailService.getTextHtml(tempPasswordDir);
		tempPasswordHTML = tempPasswordHTML
				.replaceAll("McLovin", user.getName())
				.replaceAll("password_temp", tempPassword);
		
		Map<String, String> imgs = new HashMap<>();
		imgs.put("logo", imgLogo);
		mailService.send(user.getEmail(), "Senha Temporaria", tempPasswordHTML, imgs);
		*/

		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		
		user.setActive(true);
		user.setUpdatePassword(true);

		user = userRepository.save(user);
		
		Double maxLimitDefault = (double) 81000.00;
		
		preferenceSettingService.save(new PreferenceSetting(maxLimitDefault, false, false, user));

	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void update(User user) {
		User updated = this.findById(user.getId());

		if (user.getName() != null && !user.getName().isEmpty())
			updated.setName(user.getName());

		if (user.getPassword() != null && !user.getPassword().isEmpty())
			updated.setPassword(passwordEncoder.encode(user.getPassword()));

		if (user.getRoles() != null && !user.getRoles().isEmpty())
			updated.setRoles(user.getRoles());

		userRepository.save(updated);

	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void disabled(Long id) {
		User user = this.findById(id);
		
		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ProjectException(String.format("User (ID: %s) not found", id)));
	}

	@Override
	public User findByEmail(String email, Boolean active) {

		if (active != null)
			return userRepository.findByEmailAndActive(email, active)
					.orElseThrow(() -> new ProjectException(String.format("Email %s not found on active users", email)));

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ProjectException(String.format("E-mail %s not found", email)));
	}

	@Override
	public Page<User> findByPage(Boolean active, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);

		if (active != null)
			return userRepository.findByActive(active, pageRequest);

		return userRepository.findAll(pageRequest);
	}

}
