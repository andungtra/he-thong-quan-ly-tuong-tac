package org.hcmus.tis.service;

import javax.xml.bind.JAXBException;

import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.xml.XProjectProcess;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { org.hcmus.tis.model.ProjectProcess.class })
public interface ProjectProcessService {
	public ProjectProcess convertToProjectProcess(XProjectProcess xProjectProcess) throws JAXBException;
	public ProjectProcess createProjectProcess(byte[] templateFile) throws JAXBException;
}
