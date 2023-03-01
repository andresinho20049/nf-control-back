package com.andre.nfcontrol.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andre.nfcontrol.models.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
	
	List<Partner> findByUser_Id(Long userId);
	
	Page<Partner> findByUser_Id(Long userId, Pageable pageable);
	
}
