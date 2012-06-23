package org.hcmus.tis.controller;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.ApplicationRole;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.repository.ApplicationRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
		registry.addConverter(new Converter<ApplicationRole, String>() {

			@Override
			public String convert(ApplicationRole source) {
				return source.getName();
			}
		});
		registry.addConverter(new Converter<String, ApplicationRole>() {

			@Override
			public ApplicationRole convert(String id) {
				return appRoleRepository.findOne(Long.valueOf(id));
			}
		});
	}
	@Autowired
	private ApplicationRoleRepository appRoleRepository;
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
	public Converter<MemberRole, String> getMemberRoleToStringConverter(){
		return new Converter<MemberRole, String>() {
			
			@Override
			public String convert(MemberRole arg0) {
				return arg0.getName();
			}
		};
	}
    public Converter<MemberInformation, String> getMemberInformationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.MemberInformation, java.lang.String>() {
            public String convert(MemberInformation memberInformation) {
                return memberInformation.getAccount().getEmail();
            }
        };
    }
    public Converter<WorkItemType, String> getWorkItemTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.WorkItemType, java.lang.String>() {
            public String convert(WorkItemType workItemType) {
                return workItemType.getName();
            }
        };
    }
    public Converter<ProjectProcess, String> getProjectProcessToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.ProjectProcess, java.lang.String>() {
            public String convert(ProjectProcess projectProcess) {
                return projectProcess.getName();
            }
        };
    }

}
