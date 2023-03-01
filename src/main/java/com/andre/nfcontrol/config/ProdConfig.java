package com.andre.nfcontrol.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.Roles;
import com.andre.nfcontrol.models.User;
import com.andre.nfcontrol.service.RolesService;
import com.andre.nfcontrol.service.UserService;
import com.andre.nfcontrol.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile({ "prod" })
@ComponentScan({ "com.andre.nfcontrol.controller" })
public class ProdConfig {
	
	@Value("${vibbraneo_mail}")
	private String vibbraneoMail;
	
	@Value("${vibbraneo_password}")
	private String vibbraneoPassword;

	@Autowired
	private UserService userService;

	@Autowired
	private RolesService rolesService;

	@Bean
	public void startDatabase() {
		this.initialUser();
	}

	private void initialUser() {

		String[] rolesName = { Constants.ROLE_ADMIN, Constants.ROLE_VIEW_USER, Constants.ROLE_CREATE_USER,
				Constants.ROLE_UPDATE_USER, Constants.ROLE_UPDATE_ROLES_USER, Constants.ROLE_DELETE_USER,
				Constants.ROLE_DISABLE_USER };

		Roles roles = null;
		for (String roleName : rolesName) {
			try {
				rolesService.findByName(roleName);
			} catch (ProjectException e) {
				roles = new Roles(roleName);
				rolesService.save(roles);
			}
		}

		roles = rolesService.findByName(Constants.ROLE_ADMIN);
		// Users Test
		try {
			String name = "Vibbraneo";
			User user = new User(name, vibbraneoMail, vibbraneoPassword, Arrays.asList(roles));
			userService.save(user);			
		} catch (ProjectException e) {
			log.error(e.getMessage(), e);
		}
	}

}
