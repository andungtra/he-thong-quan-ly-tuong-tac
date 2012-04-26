package org.hcmus.tis.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.model.xml.ObjectFactory;
import org.hcmus.tis.model.xml.XAdditionalFieldsImpl;
import org.hcmus.tis.model.xml.XChoices;
import org.hcmus.tis.model.xml.XField;
import org.hcmus.tis.model.xml.XFieldImpl;
import org.hcmus.tis.model.xml.XProjectProcess;
import org.hcmus.tis.model.xml.XWorkItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;


public class ProjectProcessServiceImpl implements ProjectProcessService {
	@Autowired
	private JAXBContext jaxbContext;
	public ProjectProcess convertToProjectProcess(XProjectProcess xProjectProcess) throws JAXBException{
		ProjectProcess projectProcess = new ProjectProcess();
		projectProcess.setUniqueName(xProjectProcess.getIdentity());
		projectProcess.setName(xProjectProcess.getName());
		projectProcess.setDescription(xProjectProcess.getXDescription().toString());
		projectProcess.setWorkItemTypes(new ArrayList<WorkItemType>());
		List<XWorkItem> xWorkItems = xProjectProcess.getXWorkItems().getXWorkItem();
		for(XWorkItem xWorkItem : xWorkItems){
			WorkItemType workItemType = new WorkItemType();
			workItemType.setName(xWorkItem.getName());
			workItemType.setProjectProcess(projectProcess);		
			ObjectFactory objectFactory = new ObjectFactory();
			XAdditionalFieldsImpl fieldsImpl =  objectFactory.createXAdditionalFieldsImpl();
			for(XField field : xWorkItem.getXAddionalFields().getXField()){
				XFieldImpl fieldImpl = objectFactory.createXFieldImpl();
				fieldImpl.setRef(field.getRefName());
				fieldsImpl.getXField().add(fieldImpl);
			}
			Marshaller marshaller = jaxbContext.createMarshaller();
			Writer writer = new StringWriter();
			marshaller.marshal(fieldsImpl, writer);
			workItemType.setAdditionalFieldsDefine(writer.toString());
			projectProcess.getWorkItemTypes().add(workItemType);
		}
		return projectProcess;
		
	}
	public ProjectProcess createProjectProcess(byte[] templateFile) throws JAXBException{
		InputStream inputStream = new ByteArrayInputStream(templateFile);
		Unmarshaller umMarshaller = jaxbContext.createUnmarshaller();
		StreamSource source = new StreamSource(inputStream);
		JAXBElement<XProjectProcess> element = umMarshaller.unmarshal(source, XProjectProcess.class);
		XProjectProcess xProjectProcess = element.getValue();
		ProjectProcess projectProcess = convertToProjectProcess(xProjectProcess);
		projectProcess.setProcessTemplateFile(templateFile);
		projectProcess.persist();
		return projectProcess;
	}
	public JAXBContext getJaxbImplContext() {
		return jaxbContext;
	}
	public void setJaxbImplContext(JAXBContext jaxbImplContext) {
		this.jaxbContext = jaxbImplContext;
	}
}
