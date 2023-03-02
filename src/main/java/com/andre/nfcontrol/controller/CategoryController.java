package com.andre.nfcontrol.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andre.nfcontrol.dto.StandardResponse;
import com.andre.nfcontrol.models.Category;
import com.andre.nfcontrol.service.CategoryService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/category")
@Api(value = "Category", description = "Category Controller", tags= {"category"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<?> findAll(
			@RequestParam(value = "archive", required = false) Boolean isArchive) {

		return ResponseEntity.ok(categoryService.findAll(isArchive));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		
		return ResponseEntity.ok(categoryService.findById(id));
	}
    
	@GetMapping("/paginated")
	public ResponseEntity<?> findByPage(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "archive", required = false) Boolean isArchive,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "order", defaultValue = "id") String order,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(categoryService.findByPage(search, isArchive, page, size, order, direction));
	}
	
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category){
        categoryService.save(category);
        
        String msg = String.format("Category %s registered successfully!", category.getName());
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(new StandardResponse(HttpServletResponse.SC_CREATED, msg, System.currentTimeMillis()));
    }
    
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Category category){
        categoryService.update(category);
        
        String msg = String.format("Category %s successfully updated!", category.getName());
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }
    
    @PatchMapping("/archive/{id}")
    public ResponseEntity<?> toggleArchive(@PathVariable Long id){
        categoryService.archive(id);
        
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "Category successfully archived!", System.currentTimeMillis()));
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		categoryService.delete(id);
		return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "Category successfully deleted!", System.currentTimeMillis()));
	}
}
