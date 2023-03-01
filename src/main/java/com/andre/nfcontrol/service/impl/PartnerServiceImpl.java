package com.andre.nfcontrol.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.andre.nfcontrol.exceptions.ProjectException;
import com.andre.nfcontrol.models.Partner;
import com.andre.nfcontrol.repository.PartnerRepository;
import com.andre.nfcontrol.service.PartnerService;
import com.andre.nfcontrol.utils.Pagination;
import com.andre.nfcontrol.utils.SecurityContext;

@Service
public class PartnerServiceImpl implements PartnerService {

	@Autowired
	private PartnerRepository partnerRepository;

	// I can create a ConstraintValidator implementation if the team prefers
	private void validPersist(Partner partner) {

		if (partner == null)
			throw new ProjectException("Category is null");
		
		StringBuilder nullAttributes = new StringBuilder();

		if (partner.getLegalName() == null)
			nullAttributes.append("Company Name, ");

		if (partner.getCnpj() == null)
			nullAttributes.append("CNPJ, ");

		if (partner.getShortName() == null)
			nullAttributes.append("Trade name, ");

		if(nullAttributes.length() > 0)
			throw new ProjectException(String.format("Fields %s are required", nullAttributes.substring(0, nullAttributes.length() - 2)));
	}

	@Override
	public void save(Partner partner) {

		this.validPersist(partner);
		
		partner.setUser(SecurityContext.getUserLogged());

		partnerRepository.save(partner);

	}

	@Override
	public List<Partner> findAll() {
		
		Long userId = SecurityContext.getUserLogged().getId();

		return partnerRepository.findByUser_Id(userId);
	}

	@Override
	public void update(Partner partner) {

		this.validPersist(partner);

		if (partner.getId() == null)
			throw new ProjectException("Need to inform ID for update");

		partnerRepository.save(partner);

	}

	@Override
	public void delete(Long id) {
		this.findById(id);
		
		partnerRepository.deleteById(id);
	}

	@Override
	public Partner findById(Long id) {

		return partnerRepository.findById(id)
				.orElseThrow(() -> new ProjectException("Partner id: " + id + " not found"));
	}

	@Override
	public Page<Partner> findByPage(Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);
		
		Long userId = SecurityContext.getUserLogged().getId();

		return partnerRepository.findByUser_Id(userId, pageRequest);
	}

}
