package com.sfinx.pdmm.controllers;

import static java.util.Arrays.asList;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para la gestión de aprobaciones otorgadas por el usuario
 * a las aplicaciones cliente.
 * 
 * @author marojas
 *
 */
@Controller
@RequestMapping("/approvals")
public class ApprovalController {
	
	@Autowired
	private ApprovalStore approvalStore;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private JdbcClientDetailsService clientDetailsService;
	
	@GetMapping("")
	public String approvals(Map<String, Object> model, Principal principal) {
		List<Approval> approvals = clientDetailsService.listClientDetails().stream()
                .map(clientDetail -> approvalStore.getApprovals(principal.getName(), clientDetail.getClientId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
		model.put("approvals", approvals);
		return "approvals/approval-list";
	}
	
	@PostMapping("/revoke")
	public String revokeApproval(@ModelAttribute Approval approval) {
		approvalStore.revokeApprovals(asList(approval));
		tokenStore
			.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId())
			.forEach(tokenStore::removeAccessToken);
		return "redirect:/approvals";
	}

}
