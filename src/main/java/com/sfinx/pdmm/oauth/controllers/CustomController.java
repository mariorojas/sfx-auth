package com.sfinx.pdmm.oauth.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Controlador para mostrar pantallas personalizadas de seguridad OAuth2
 * como formulario para inicio de sesión, página de aprobaciones de usuario
 * y página de denegación por falta de permisos.
 * 
 * @author marojas
 *
 */
@Controller
@SessionAttributes("authorizationRequest")
public class CustomController {
	
	@RequestMapping("/login")
	public String login() {
		return "security/login";
	}
	
	@RequestMapping("/unauthorized")
	public String error403() {
		return "unauthorized";
	}
	
	@RequestMapping("/oauth/confirm_access")
	public String userApproval(Map<String, Object> model, HttpServletRequest request) {
		if (model.containsKey("scopes") || request.getAttribute("scopes")!=null) {
			if ( ! model.containsKey("scopes")) {
				model.put("scopes", request.getAttribute("scopes"));
				model.put("noScopes", false);
			}
		} else {
			model.put("noScopes", true);
		}
		return "security/confirm_access";
	}

}
