package com.andre.nfcontrol.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andre.nfcontrol.dto.StandardResponse;
import com.andre.nfcontrol.models.PreferenceSetting;
import com.andre.nfcontrol.service.PreferenceSettingService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/preference-setting")
@Api(value = "PreferenceSetting", description = "Preference Setting Controller", tags= {"preference-setting"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class PreferenceSettingController {
	
	@Autowired
	private PreferenceSettingService preferenceSettingService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		
		return ResponseEntity.ok(preferenceSettingService.findById(id));
	}
	
	@GetMapping("/find-by-user")
	public ResponseEntity<?> findByUserId() {
		
		return ResponseEntity.ok(preferenceSettingService.findByUserLogged());
	}
    
    @PutMapping
    public ResponseEntity<?> update(@RequestBody PreferenceSetting preferenceSetting){
        preferenceSettingService.update(preferenceSetting);
        
        String msg = String.format("Preference Setting by User %s successfully updated!", preferenceSetting.getUser().getName());
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		preferenceSettingService.delete(id);
		return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "Preference Setting successfully deleted!", System.currentTimeMillis()));
	}
}
