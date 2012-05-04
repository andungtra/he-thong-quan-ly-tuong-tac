package org.hcmus.tis.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@RooIntegrationTest(entity = WorkItemType.class)
public class WorkItemTypeIntegrationTest {
	private WorkItemType aut;

	@Before
	public void setUp() {
		aut = new WorkItemType();
	}

	@Test
	public void testMarkerMethod() {
	}

	@Test
	public void testGetAdditionalFieldDefines()
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException, JAXBException {
		File xmlFile = new File("src/test/java/ScrumTemplate.xml");
		RandomAccessFile templateFile = new RandomAccessFile(
				"src/test/java/ScrumTemplate.xml", "r");
		byte[] b = new byte[(int) templateFile.length()];
		templateFile.read(b);
		ProjectProcess projectProcess = new ProjectProcess();
		projectProcess.setProcessTemplateFile(b);
		JAXBContext jaxbContext = JAXBContext.newInstance("org.hcmus.tis.model.xml");
		aut.setJaxbContext(jaxbContext);
		aut.setRefName("defect");
		WorkItemType spyWorkItemType = Mockito.spy(aut);
		Mockito.doReturn(projectProcess).when(spyWorkItemType).getProjectProcess();
		
		List<FieldDefine> additionalFiels = spyWorkItemType.getAdditionalFieldDefines();
		assertEquals(3, additionalFiels.size());
		assertEquals("environment", additionalFiels.get(1).getRefName());
		assertEquals(true, additionalFiels.get(1).isRequired());
		assertEquals(false, additionalFiels.get(0).isRequired());
		assertEquals("defaultvalue", additionalFiels.get(1).getDefaultValue());
		assertEquals(BuildInType.string, additionalFiels.get(1).getType());
		Assert.assertNotNull(additionalFiels.get(0).getChoices());
		assertEquals(2, additionalFiels.get(0).getChoices().size());
		assertEquals("Critical", additionalFiels.get(0).getChoices().get(0));
	}
}
