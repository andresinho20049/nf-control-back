package com.andre.nfcontrol.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.Invoice;
import com.andre.nfcontrol.repository.InvoiceRepository;
import com.andre.nfcontrol.service.InvoiceService;
import com.andre.nfcontrol.utils.Pagination;
import com.andre.nfcontrol.utils.SecurityContext;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;

	// I can create a ConstraintValidator implementation if the team prefers
	private void validPersist(Invoice invoice) {

		if (invoice == null)
			throw new ProjectException("Category is null");

		if (invoice.getInvoiceNumber() == null)
			throw new ProjectException("Invoice Number is required");

		if (invoice.getAccrualDate() == null)
			throw new ProjectException("Accrual Date is required");

		if (invoice.getDueDate() == null)
			throw new ProjectException("Due Date is required");

		if (invoice.getPartner() == null || invoice.getPartner().getId() == null)
			throw new ProjectException("Partner is required");

		if (invoice.getValue() == null)
			throw new ProjectException("Value is required");

	}

	@Override
	public void save(Invoice invoice) {

		this.validPersist(invoice);
		
		invoice.setUser(SecurityContext.getUserLogged());

		invoiceRepository.save(invoice);

	}

	@Override
	public List<Invoice> findAll(Integer year) {
		
		Long userId = SecurityContext.getUserLogged().getId();
		
		if(year != null)
			return invoiceRepository.findByYearAndUser(year, userId);

		return invoiceRepository.findByUser_Id(userId);
	}

	@Override
	public void update(Invoice invoice) {

		this.validPersist(invoice);

		if (invoice.getId() == null)
			throw new ProjectException("Need to inform ID for update");

		invoiceRepository.save(invoice);

	}

	@Override
	public void delete(Long id) {
		this.findById(id);
		
		invoiceRepository.deleteById(id);
	}

	@Override
	public Invoice findById(Long id) {

		return invoiceRepository.findById(id)
				.orElseThrow(() -> new ProjectException("Invoice id: " + id + " not found"));
	}

	@Override
	public Page<Invoice> findByPage(Integer year, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);
		
		Long userId = SecurityContext.getUserLogged().getId();

		if(year != null)
			return invoiceRepository.findByYearAndUser(year, userId, pageRequest);
		
		return invoiceRepository.findByUser_Id(userId, pageRequest);
	}

}
