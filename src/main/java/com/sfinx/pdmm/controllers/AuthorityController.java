package com.sfinx.pdmm.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sfinx.pdmm.dto.AuthorityDTO;
import com.sfinx.pdmm.oauth.domain.Authority;
import com.sfinx.pdmm.oauth.services.AuthorityRepository;

/**
 * Controlador para gestión de roles
 * 
 * @author marojas
 *
 */
@Controller
@RequestMapping("/authorities")
public class AuthorityController {
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@RequestMapping("")
	public String authorities(Map<String, Object> model) {
		model.put("authorities", authorityRepository.findAllByOrderByAuthority());
		return "authorities/authorities-list";
	}
	
	@GetMapping("/form")
	public String showAddOrUpdateForm(Map<String, Object> model,
			@RequestParam(name ="authority", required = false) String authorityId) {
		Authority authority = null;
		if (authorityId != null) {
			authority = authorityRepository.findOne(Long.parseLong(authorityId));
		} else {
			authority = new Authority();
		}
		model.put("authority", authority);
		return "authorities/form";
	}
	
	@PostMapping("/edit")
	public String addOrUpdateUser(@ModelAttribute AuthorityDTO dto) {
		Authority authority = new Authority();
		if (dto.getId() != null) {
			authority = authorityRepository.findOne(dto.getId());
		} 
		authority.setAuthority(dto.getAuthority());
		authorityRepository.save(authority);
		return "redirect:/authorities";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam(name = "authority") String id) {
		Authority authority = authorityRepository.findOne(Long.parseLong(id));
		if (authority != null) {
			authorityRepository.delete(authority);
		}
		return "redirect:/authorities";
	}

}
