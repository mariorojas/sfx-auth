package com.sfinx.pdmm.controllers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sfinx.pdmm.oauth.config.AuthorityPropertyEditor;
import com.sfinx.pdmm.oauth.config.SplitCollectionEditor;
import com.sfinx.pdmm.oauth.services.UserRepository;

/**
 * Controlador para la gestión de los clientes permitidos en la autenticación.
 * 
 * @author marojas
 *
 */
@Controller
@RequestMapping("/clients")
public class ClientController {
	
	@Autowired
	private JdbcClientDetailsService clientDetailsService;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ApprovalStore approvalStore;
	
	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Necesario para convertir y desplegar los roles como cadenas
		binder.registerCustomEditor(GrantedAuthority.class, new AuthorityPropertyEditor());
		
		// Es requerido para el vector GrantedAuthority, si no se utiliza este editor, los roles
		// se mostrarán como un vector [null] en lugar de []
		binder.registerCustomEditor(Collection.class, new SplitCollectionEditor(Set.class, ","));
	}
	
	/**
	 * Despliega el listado de todos los clientes autorizados para conectar
	 * 
	 * @param model						Parámetros a inyectar en la vista
	 * @return
	 */
	@GetMapping("")
	public String clients(Map<String, Object> model) {
		model.put("clients", clientDetailsService.listClientDetails());
		return "clients/client-list";
	}
	
	/**
	 * Despliega el formulario de edición para las propiedades de un
	 * cliente en base a su identificador
	 * 
	 * @param model						Parámetros a inyectar en la vista
	 * @param clientId					Identificador del cliente
	 * @return
	 */
	@GetMapping("/form")
	public String showAddOrUpdateForm(Map<String, Object> model, 
			@RequestParam(name = "client", required = false) String clientId) {
		ClientDetails clientDetails = null;
		if (clientId != null) {
			clientDetails = clientDetailsService.loadClientByClientId(clientId);
		} else {
			clientDetails = new BaseClientDetails();
		}
		model.put("client", clientDetails);
		return "clients/form";
	}
	
	/**
	 * Guarda las propiedades de un cliente
	 * 
	 * @param clientDetails
	 * @return
	 */
	@PostMapping("/edit")
	public String addOrUpdateClient(@ModelAttribute BaseClientDetails clientDetails,
			@RequestParam(value = "newClient", required = false) String newClient) {
		if (newClient == null) {
			removeTokens(clientDetails);
			removeApprovals(clientDetails);
			clientDetailsService.updateClientDetails(clientDetails);
		} else {
			clientDetailsService.addClientDetails(clientDetails);
		}
		
		// En caso de que el usuario haya indicado una clave
		if ( ! clientDetails.getClientSecret().isEmpty()) {
			clientDetailsService.updateClientSecret(clientDetails.getClientId(), clientDetails.getClientSecret());
		}
		
		return "redirect:/clients";
	}
	
	@GetMapping("/delete")
	public String deleteClient(@RequestParam(value = "client", required = true) String clientId) {
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
		if (clientDetails != null) {
			removeTokens(clientDetails);
			removeApprovals(clientDetails);
		}
		clientDetailsService.removeClientDetails(clientId);
		return "redirect:/clients";
	}
	
	/**
	 * Elimina los tokens de acceso y actualización
	 * 
	 * @param clientDetails
	 */
	private void removeTokens(ClientDetails clientDetails) {
		tokenStore
		.findTokensByClientId(clientDetails.getClientId())
		.forEach(token -> {
			tokenStore.removeAccessToken(token);
			if (token.getRefreshToken() != null)
				tokenStore.removeRefreshToken(token.getRefreshToken());
		});
	}
	
	/**
	 * Elimina las aprobaciones de todos los usuarios involucrados con el cliente
	 * @param clientDetails
	 */
	private void removeApprovals(ClientDetails clientDetails) {
		userRepository.findAll().forEach(user -> {
			Collection<Approval> approvals = approvalStore.getApprovals(user.getUsername(), clientDetails.getClientId());
			approvalStore.revokeApprovals(approvals);
		});
	}

}
