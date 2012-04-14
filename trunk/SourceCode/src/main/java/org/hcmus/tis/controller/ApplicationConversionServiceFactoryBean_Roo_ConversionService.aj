// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import org.hcmus.tis.controller.ApplicationConversionServiceFactoryBean;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    @Autowired
    AccountService ApplicationConversionServiceFactoryBean.accountService;
    
    public Converter<Long, Account> ApplicationConversionServiceFactoryBean.getIdToAccountConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.Account>() {
            public org.hcmus.tis.model.Account convert(java.lang.Long id) {
                return accountService.findAccount(id);
            }
        };
    }
    
    public Converter<String, Account> ApplicationConversionServiceFactoryBean.getStringToAccountConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.Account>() {
            public org.hcmus.tis.model.Account convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Account.class);
            }
        };
    }
    
    public Converter<MemberInformation, String> ApplicationConversionServiceFactoryBean.getMemberInformationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.MemberInformation, java.lang.String>() {
            public String convert(MemberInformation memberInformation) {
                return new StringBuilder().toString();
            }
        };
    }
    
    public Converter<Long, MemberInformation> ApplicationConversionServiceFactoryBean.getIdToMemberInformationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.MemberInformation>() {
            public org.hcmus.tis.model.MemberInformation convert(java.lang.Long id) {
                return MemberInformation.findMemberInformation(id);
            }
        };
    }
    
    public Converter<String, MemberInformation> ApplicationConversionServiceFactoryBean.getStringToMemberInformationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.MemberInformation>() {
            public org.hcmus.tis.model.MemberInformation convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MemberInformation.class);
            }
        };
    }
    
    public Converter<Long, MemberRole> ApplicationConversionServiceFactoryBean.getIdToMemberRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.MemberRole>() {
            public org.hcmus.tis.model.MemberRole convert(java.lang.Long id) {
                return MemberRole.findMemberRole(id);
            }
        };
    }
    
    public Converter<String, MemberRole> ApplicationConversionServiceFactoryBean.getStringToMemberRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.MemberRole>() {
            public org.hcmus.tis.model.MemberRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MemberRole.class);
            }
        };
    }
    
    public Converter<Priority, String> ApplicationConversionServiceFactoryBean.getPriorityToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.Priority, java.lang.String>() {
            public String convert(Priority priority) {
                return new StringBuilder().append(priority.getName()).toString();
            }
        };
    }
    
    public Converter<Long, Priority> ApplicationConversionServiceFactoryBean.getIdToPriorityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.Priority>() {
            public org.hcmus.tis.model.Priority convert(java.lang.Long id) {
                return Priority.findPriority(id);
            }
        };
    }
    
    public Converter<String, Priority> ApplicationConversionServiceFactoryBean.getStringToPriorityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.Priority>() {
            public org.hcmus.tis.model.Priority convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Priority.class);
            }
        };
    }
    
    public Converter<Long, Project> ApplicationConversionServiceFactoryBean.getIdToProjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.Project>() {
            public org.hcmus.tis.model.Project convert(java.lang.Long id) {
                return Project.findProject(id);
            }
        };
    }
    
    public Converter<String, Project> ApplicationConversionServiceFactoryBean.getStringToProjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.Project>() {
            public org.hcmus.tis.model.Project convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Project.class);
            }
        };
    }
    
    public Converter<ProjectProcess, String> ApplicationConversionServiceFactoryBean.getProjectProcessToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.ProjectProcess, java.lang.String>() {
            public String convert(ProjectProcess projectProcess) {
                return new StringBuilder().append(projectProcess.getName()).append(" ").append(projectProcess.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, ProjectProcess> ApplicationConversionServiceFactoryBean.getIdToProjectProcessConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.ProjectProcess>() {
            public org.hcmus.tis.model.ProjectProcess convert(java.lang.Long id) {
                return ProjectProcess.findProjectProcess(id);
            }
        };
    }
    
    public Converter<String, ProjectProcess> ApplicationConversionServiceFactoryBean.getStringToProjectProcessConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.ProjectProcess>() {
            public org.hcmus.tis.model.ProjectProcess convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ProjectProcess.class);
            }
        };
    }
    
    public Converter<Long, StudyClass> ApplicationConversionServiceFactoryBean.getIdToStudyClassConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.StudyClass>() {
            public org.hcmus.tis.model.StudyClass convert(java.lang.Long id) {
                return StudyClass.findStudyClass(id);
            }
        };
    }
    
    public Converter<String, StudyClass> ApplicationConversionServiceFactoryBean.getStringToStudyClassConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.StudyClass>() {
            public org.hcmus.tis.model.StudyClass convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), StudyClass.class);
            }
        };
    }
    
    public Converter<WorkItem, String> ApplicationConversionServiceFactoryBean.getWorkItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.WorkItem, java.lang.String>() {
            public String convert(WorkItem workItem) {
                return new StringBuilder().append(workItem.getTitle()).append(" ").append(workItem.getDescription()).append(" ").append(workItem.getDateCreated()).append(" ").append(workItem.getAdditionalFields()).toString();
            }
        };
    }
    
    public Converter<Long, WorkItem> ApplicationConversionServiceFactoryBean.getIdToWorkItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.WorkItem>() {
            public org.hcmus.tis.model.WorkItem convert(java.lang.Long id) {
                return WorkItem.findWorkItem(id);
            }
        };
    }
    
    public Converter<String, WorkItem> ApplicationConversionServiceFactoryBean.getStringToWorkItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.WorkItem>() {
            public org.hcmus.tis.model.WorkItem convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), WorkItem.class);
            }
        };
    }
    
    public Converter<WorkItemType, String> ApplicationConversionServiceFactoryBean.getWorkItemTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.hcmus.tis.model.WorkItemType, java.lang.String>() {
            public String convert(WorkItemType workItemType) {
                return new StringBuilder().append(workItemType.getName()).append(" ").append(workItemType.getAdditionalFieldsDefine()).toString();
            }
        };
    }
    
    public Converter<Long, WorkItemType> ApplicationConversionServiceFactoryBean.getIdToWorkItemTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.hcmus.tis.model.WorkItemType>() {
            public org.hcmus.tis.model.WorkItemType convert(java.lang.Long id) {
                return WorkItemType.findWorkItemType(id);
            }
        };
    }
    
    public Converter<String, WorkItemType> ApplicationConversionServiceFactoryBean.getStringToWorkItemTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.hcmus.tis.model.WorkItemType>() {
            public org.hcmus.tis.model.WorkItemType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), WorkItemType.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getAccountToStringConverter());
        registry.addConverter(getIdToAccountConverter());
        registry.addConverter(getStringToAccountConverter());
        registry.addConverter(getMemberInformationToStringConverter());
        registry.addConverter(getIdToMemberInformationConverter());
        registry.addConverter(getStringToMemberInformationConverter());
        registry.addConverter(getMemberRoleToStringConverter());
        registry.addConverter(getIdToMemberRoleConverter());
        registry.addConverter(getStringToMemberRoleConverter());
        registry.addConverter(getPriorityToStringConverter());
        registry.addConverter(getIdToPriorityConverter());
        registry.addConverter(getStringToPriorityConverter());
        registry.addConverter(getProjectToStringConverter());
        registry.addConverter(getIdToProjectConverter());
        registry.addConverter(getStringToProjectConverter());
        registry.addConverter(getProjectProcessToStringConverter());
        registry.addConverter(getIdToProjectProcessConverter());
        registry.addConverter(getStringToProjectProcessConverter());
        registry.addConverter(getStudyClassToStringConverter());
        registry.addConverter(getIdToStudyClassConverter());
        registry.addConverter(getStringToStudyClassConverter());
        registry.addConverter(getWorkItemToStringConverter());
        registry.addConverter(getIdToWorkItemConverter());
        registry.addConverter(getStringToWorkItemConverter());
        registry.addConverter(getWorkItemTypeToStringConverter());
        registry.addConverter(getIdToWorkItemTypeConverter());
        registry.addConverter(getStringToWorkItemTypeConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
