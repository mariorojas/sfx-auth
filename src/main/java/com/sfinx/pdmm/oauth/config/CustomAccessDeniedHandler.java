package com.sfinx.pdmm.oauth.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Manejo de excepción para las peticiones que no tienen suficientes
 * permisos para acceder a un recurso.
 * 
 * @author marojas
 *
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	private Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			log.info("El usuario " + auth.getName()
					+ " ha tratado de acceder al recurso protegido "
					+ request.getRequestURI());
		}
		response.sendRedirect(request.getContextPath() + "/unauthorized");
	}

}
