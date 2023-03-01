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
import com.andre.nfcontrol.models.Expense;
import com.andre.nfcontrol.service.ExpenseService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/expense")
@Api(value = "Expense", description = "Expense Controller", tags= {"expense"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping
	public ResponseEntity<?> findAll(
			@RequestParam(value = "year", required = false) Integer year) {
		
		return ResponseEntity.ok(expenseService.findAll(year));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		
		return ResponseEntity.ok(expenseService.findById(id));
	}
    
	@GetMapping("/paginated")
	public ResponseEntity<?> findByPage(
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "order", defaultValue = "id") String order,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(expenseService.findByPage(year, page, size, order, direction));
	}
	
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Expense expense){
        expenseService.save(expense);
        
        String msg = String.format("Expense %s registered successfully!", expense.getName());
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(new StandardResponse(HttpServletResponse.SC_CREATED, msg, System.currentTimeMillis()));
    }
    
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Expense expense){
        expenseService.update(expense);
        
        String msg = String.format("Expense %s successfully updated!", expense.getName());
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		expenseService.delete(id);
		return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "Expense successfully deleted!", System.currentTimeMillis()));
	}
}
