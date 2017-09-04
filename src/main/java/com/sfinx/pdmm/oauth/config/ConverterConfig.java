package com.sfinx.pdmm.oauth.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

/**
 * Convertidor para el formato yyyy-MM-dd HH:mm:ss
 * 
 * Es necesario para revocar aprobaciones ya que necesita ligar los campos
 * expiresAt y lastUpdateAt de cada aprobación.
 *  
 * @author marojas
 *
 */
@Configuration
public class ConverterConfig {
	
	@Bean
	public Converter<String, Date> stringDateConverter() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				if (source == null) {
					throw new IllegalArgumentException("La fecha no puede ser nula");
				}
				try {
					return simpleDateFormat.parse(source);
				} catch (ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}
		};
	}

}
