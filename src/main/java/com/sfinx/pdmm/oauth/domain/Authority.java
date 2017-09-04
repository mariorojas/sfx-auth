package com.sfinx.pdmm.oauth.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import org.springframework.security.core.GrantedAuthority;

/**
 * Entidad para persistencia de roles.
 * 
 * @author marojas
 *
 */
@Data
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "authorities_id_gen", sequenceName = "authorities_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_id_gen")
	private Long id;
	
	private String authority;

	@Override
	public String getAuthority() {
		return authority;
	}

}
