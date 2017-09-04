package com.sfinx.pdmm.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sfinx.pdmm.oauth.domain.Authority;
import com.sfinx.pdmm.oauth.domain.User;
import com.sfinx.pdmm.oauth.services.AuthorityRepository;
import com.sfinx.pdmm.oauth.services.UserRepository;

/**
 * Controlador para la gestión de usuarios permitidos para autenticar,
 * solamente el usuario con el permiso ROLE_OAUTH_ADMIN tiene permitido
 * modificar el listado.
 * 
 * @author marojas
 *
 */
@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@RequestMapping("")
	public String users(Map<String, Object> model) {
		model.put("users", userRepository.findAllByOrderByUsername());
		return "users/user-list";
	}
	
	@GetMapping("/form")
	public String showAddOrUpdateForm(Map<String, Object> model,
			@RequestParam(name ="user", required = false) String userId) {
		User user = null;
		if (userId != null) {
			user = userRepository.findOne(Long.parseLong(userId));
		} else {
			user = new User();
		}
		List<Authority> authorities = authorityRepository.findAllByOrderByAuthority();
		model.put("user", user);
		model.put("authorities", authorities);
		return "users/form";
	}
	
	@PostMapping("/edit")
	public String addOrUpdateUser(@ModelAttribute User user) {
		if (user.getId() == null) {
			user.setEnabled(Boolean.TRUE);
			user.setPassword(generatePassword(user.getPassword()));
		} else {
			User storedUser = userRepository.findOne(user.getId());
			user.setEnabled(storedUser.getEnabled());
			if ( ! user.getPassword().isEmpty()) {
				user.setPassword(generatePassword(user.getPassword()));
			} else {
				user.setPassword(storedUser.getPassword());
			}
		}
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@RequestMapping("/status")
	public String enableUser(@RequestParam(name = "user", required = true) String id, 
			@RequestParam(name = "status", required = true) String status) {
		User user = userRepository.findOne(Long.parseLong(id));
		if (user != null) {
			user.setEnabled(Boolean.parseBoolean(status));
			userRepository.save(user);
		}
		return "redirect:/users";
	}
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam(name = "user") String id) {
		User user = userRepository.findOne(Long.parseLong(id));
		if (user != null) {
			userRepository.delete(user);
		}
		return "redirect:/users";
	}
	
	private String generatePassword(String rawPassword) {
		String encryptedPassword = new BCryptPasswordEncoder().encode(rawPassword);
		return encryptedPassword;
	}

}
