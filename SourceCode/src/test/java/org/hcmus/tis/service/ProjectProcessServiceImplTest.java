package org.hcmus.tis.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javassist.bytecode.ByteArray;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.model.xml.XProjectProcess;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;
@MockStaticEntityMethods
public class ProjectProcessServiceImplTest {
	private ProjectProcessServiceImpl aut;
	@Before
	public void setUp() throws Exception {
		aut = new ProjectProcessServiceImpl();
	}

/*	public void testCreateProjectProcess() throws ParserConfigurationException, SAXException, IOException, JAXBException {
			File xmlFile = new File("src/test/java/ScrumTemplate.xml");
			JAXBContext context = JAXBContext.newInstance("org.hcmus.tis.model.xml");
			Unmarshaller umMarshaller = context.createUnmarshaller();
			StreamSource source = new StreamSource(xmlFile);
			JAXBElement<XProjectProcess> element = umMarshaller.unmarshal(source, XProjectProcess.class);
			XProjectProcess xProjectProcess = element.getValue();			
			aut.setJaxbImplContext(context);
			aut.createProjectProcess(xProjectProcess);
			
	}*/
	@Test
	public void testCreate() throws JAXBException{
		
		
		JAXBContext mockedJaxbContext = Mockito.mock(JAXBContext.class);
		Unmarshaller mockedUnmarshller = Mockito.mock(Unmarshaller.class);
		Mockito.doReturn(mockedUnmarshller).when(mockedJaxbContext).createUnmarshaller();
		JAXBElement<XProjectProcess> mockedXProjectProcess = Mockito.mock(JAXBElement.class);
		Mockito.doReturn(new XProjectProcess()).when(mockedXProjectProcess).getValue();
		Mockito.doReturn(mockedXProjectProcess).when(mockedUnmarshller).unmarshal(Mockito.any(StreamSource.class), Mockito.eq(XProjectProcess.class));
		aut.setJaxbImplContext(mockedJaxbContext);	
		
		ProjectProcessService spy = Mockito.spy(aut);
		byte[] templateFile = new byte[5];
		ProjectProcess mockedProjectProcess = Mockito.mock(ProjectProcess.class);
		Mockito.doReturn(mockedProjectProcess).when(spy).convertToProjectProcess(Mockito.any(XProjectProcess.class));

		
		spy.createProjectProcess(templateFile);
		
		
		Mockito.verify(spy).convertToProjectProcess(Mockito.any(XProjectProcess.class));
		Mockito.verify(mockedProjectProcess).setProcessTemplateFile(templateFile);
		Mockito.verify(mockedProjectProcess).persist();
	}
	@Test
	public void testConvertToProjectProcess() throws JAXBException{
		File xmlFile = new File("src/test/java/ScrumTemplate.xml");
		JAXBContext context = JAXBContext.newInstance("org.hcmus.tis.model.xml");
		Unmarshaller umMarshaller = context.createUnmarshaller();
		StreamSource source = new StreamSource(xmlFile);
		JAXBElement<XProjectProcess> element = umMarshaller.unmarshal(source, XProjectProcess.class);
		XProjectProcess xProjectProcess = element.getValue();			
		aut.setJaxbImplContext(context);
		ProjectProcess projectProcess = aut.convertToProjectProcess(xProjectProcess);
		assertEquals("waterfall", projectProcess.getUniqueName());
		assertEquals("Waterfall", projectProcess.getName());
		assertEquals(2, projectProcess.getWorkItemTypes().size());
	}

}
