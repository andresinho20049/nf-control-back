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
import com.andre.nfcontrol.models.Invoice;
import com.andre.nfcontrol.service.InvoiceService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/invoice")
@Api(value = "Invoice", description = "Invoice Controller", tags= {"invoice"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@GetMapping
	public ResponseEntity<?> findAll(
			@RequestParam(value = "year", required = false) Integer year) {
		
		return ResponseEntity.ok(invoiceService.findAll(year));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		
		return ResponseEntity.ok(invoiceService.findById(id));
	}
    
	@GetMapping("/paginated")
	public ResponseEntity<?> findByPage(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "order", defaultValue = "id") String order,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(invoiceService.findByPage(year, page, size, order, direction));
	}
	
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Invoice invoice){
        invoiceService.save(invoice);
        
        String msg = String.format("Tax Invoice %s registered successfully!", invoice.getInvoiceNumber());
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(new StandardResponse(HttpServletResponse.SC_CREATED, msg, System.currentTimeMillis()));
    }
    
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Invoice invoice){
        invoiceService.update(invoice);
        
        String msg = String.format("Tax Invoice %s successfully updated!", invoice.getInvoiceNumber());
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		invoiceService.delete(id);
		return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "Tax Invoice successfully deleted!", System.currentTimeMillis()));
	}
}
