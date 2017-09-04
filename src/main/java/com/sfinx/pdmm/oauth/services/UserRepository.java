package com.sfinx.pdmm.oauth.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfinx.pdmm.oauth.domain.User;

/**
 * Repositorio para persistencia de usuarios.
 * 
 * @author marojas
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * Obtiene un usuario en base a su nombre
	 * @param username					Nombre de usuario
	 * @return							Usuario
	 */
	public User findOneByUsername(String username);
	
	/**
	 * Obtiene el listado de todos los usuarios ordenados por su nombre
	 * 
	 * @return							Listado de usuarios
	 */
	public List<User> findAllByOrderByUsername();

}
