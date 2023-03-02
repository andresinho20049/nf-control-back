package com.andre.nfcontrol.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andre.nfcontrol.dto.StandardResponse;
import com.andre.nfcontrol.models.Partner;
import com.andre.nfcontrol.service.PartnerService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/partner")
@Api(value = "Partner", description = "Partner Controller", tags= {"partner"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class PartnerController {
	
	@Autowired
	private PartnerService partnerService;
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		
		return ResponseEntity.ok(partnerService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		
		return ResponseEntity.ok(partnerService.findById(id));
	}
    
	@GetMapping("/paginated")
	public ResponseEntity<?> findByPage(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "order", defaultValue = "id") String order,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(partnerService.findByPage(search, page, size, order, direction));
	}
	
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Partner partner){
        partnerService.save(partner);
        
        String msg = String.format("Partner %s registered successfully!", partner.getShortName());
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(new StandardResponse(HttpServletResponse.SC_CREATED, msg, System.currentTimeMillis()));
    }
    
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Partner partner){
        partnerService.update(partner);
        
        String msg = String.format("Partner %s successfully updated!", partner.getShortName());
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		partnerService.delete(id);
		return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "Partner successfully deleted!", System.currentTimeMillis()));
	}
}
