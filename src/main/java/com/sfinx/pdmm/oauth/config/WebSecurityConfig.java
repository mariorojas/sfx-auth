package com.sfinx.pdmm.oauth.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuración para los accesos desde navegador a la gestión de la autenticación,
 * por ahora tiene definido los accesos de usuarios en base de datos mediante
 * el servicio UserDetailsService.
 * 
 * @author marojas
 *
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // Se quita en caso de que en la misma aplicación no exista servidor de recursos
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * Obtiene desde la base de datos los usuarios que se autenticarán
	 * con el servidor OAuth
	 */
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * Establece un manejador para los errores HTTP de tipo 403
	 */
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/css/**", "/js/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/login", "/", "/logout").permitAll()
				.antMatchers("/authorities/**", "/clients/**", "/users/**").hasRole("OAUTH_ADMIN")
				.anyRequest().authenticated()
			.and().formLogin().loginPage("/login")
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).addLogoutHandler(buildLogoutHandler())
			.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
			.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and().userDetailsService(userDetailsService);
	}
	
	/**
	 * Gestiona el redireccionamiento mediante el parámetro redirect_uri al cerrar sesión
	 * 
	 * @return
	 */
	private LogoutHandler buildLogoutHandler() {
		return new LogoutHandler() {
			
			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
				String redirectUri = request.getParameter("redirect_uri");
				if (redirectUri!=null && !redirectUri.isEmpty()) {
					try {
						response.sendRedirect(redirectUri);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
			}
		};
	}

}
