package com.sfinx.pdmm.oauth.controllers;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para obtener información de un usuario autenticado
 * 
 * @author marojas
 *
 */
@RestController
public class UserInfoController {
	
	@RequestMapping("/me")
	public Principal me(Principal principal) {
		return principal;
	}

}
