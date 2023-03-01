package com.andre.nfcontrol.service.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.User;
import com.andre.nfcontrol.repository.UserRepository;
import com.andre.nfcontrol.service.MailService;
import com.andre.nfcontrol.service.UserService;
import com.andre.nfcontrol.utils.Pagination;

@Service
public class UserServiceImpl implements UserService {
	
	@Value("${directory.template.img.logo}")
	private String imgLogo;
	
	@Value("${directory.template.temppassword}")
	private String tempPasswordDir;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	
	private String generatedRandomPassword() {

		String value = UUID.randomUUID().toString();
		String sha1 = null;

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(value.getBytes("utf8"));
			sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			throw new ProjectException(e);
		}

		return sha1;
	}

	@Override
	public void save(User user) {

		if (user == null)
			throw new ProjectException("User is null");

		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new ProjectException("E-mail is required");

		if (userRepository.existsByEmail(user.getEmail()))
			throw new ProjectException("Email is already registered");
		
		String tempPassword = this.generatedRandomPassword().substring(0, 8);
		
		//Send mail: Temp Password
		String tempPasswordHTML = mailService.getTextHtml(tempPasswordDir);
		tempPasswordHTML = tempPasswordHTML
				.replaceAll("McLovin", user.getName())
				.replaceAll("password_temp", tempPassword);
		
		Map<String, String> imgs = new HashMap<>();
		imgs.put("logo", imgLogo);
		mailService.send(user.getEmail(), "Senha Temporaria", tempPasswordHTML, imgs);
		//Send ok

		String password = passwordEncoder.encode(tempPassword);
		user.setPassword(password);
		
		user.setActive(true);
		user.setUpdatePassword(true);

		userRepository.save(user);

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
