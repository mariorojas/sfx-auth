package com.sfinx.pdmm.oauth.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfinx.pdmm.oauth.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	/**
	 * Obtiene el listado de roles ordenados por su nombre
	 * 
	 * @return					Listado de roles
	 */
	public List<Authority> findAllByOrderByAuthority();

}
