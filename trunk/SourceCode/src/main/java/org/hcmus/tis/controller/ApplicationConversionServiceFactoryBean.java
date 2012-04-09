package org.hcmus.tis.controller;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.StudyClass;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {
			 
	@SuppressWarnings("deprecation")
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		
	}
	
	public Converter<StudyClass, String> getStudyClassToStringConverter() {
		return new Converter<StudyClass, String>() {
			
			@Override
			public String convert(StudyClass source) {
				// TODO Auto-generated method stub
				return source.getName();
			}
		};
		
	}
	public Converter<Account, String> getAccountToStringConverter()
	
	{
		return new Converter<Account, String>() {
			
			@Override
			public String convert(Account source) {
				// TODO Auto-generated method stub
				return source.getEmail();
			}
		};
	}
	public Converter<Project, String>getProjectToStringConverter()
	{
		return new Converter<Project, String>() {
			
			@Override
			public String convert(Project source) {
				// TODO Auto-generated method stub
				return source.getName();
			}
		};
	}

}
