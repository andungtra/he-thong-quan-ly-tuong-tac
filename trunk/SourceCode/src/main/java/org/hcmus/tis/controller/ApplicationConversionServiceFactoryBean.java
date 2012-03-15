package org.hcmus.tis.controller;

import org.hcmus.tis.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		
	}
	public Converter<Account, String> getAccountConverter() {
		return new Converter<Account, String>() {
			public String convert (Account account)
			{
				return account.getEmail();
			}
		};
		
	}
}
