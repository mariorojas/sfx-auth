package com.sfinx.pdmm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para la página de bienvenida.
 * 
 * @author marojas
 *
 */
@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String welcome() {
		return "welcome";
	}

}
