package com.sfinx.pdmm.oauth.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * Configuración del servidor de recursos con los permisos necesarios
 * por cada petición HTTP, además de indicar el identificador de recursos
 * (resource_id) con la que trabajará la petición/autenticación.
 * 
 * También incluye las cabeceras necesarias para evitar errores con
 * el filtrado de peticiones CORS.
 * 
 * @author marojas
 *
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/me").authorizeRequests().anyRequest().permitAll()
			.and().headers().addHeaderWriter((request, response) -> {
				response.addHeader("Access-Control-Allow-Origin", "*");
				if (request.getMethod().equals("OPTIONS")) {
					response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
					response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
				}
			});
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		TokenStore tokenStore = new JdbcTokenStore(dataSource);
		resources
			.resourceId("pdmm")
			.tokenStore(tokenStore);
	}

}
