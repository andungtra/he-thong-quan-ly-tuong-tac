package org.hcmus.tis.model.unittest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.Field;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.xml.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
@RunWith(PowerMockRunner.class)
public class WorkItemTest {
	@Mock
	private TypedQuery<Long> countQuery;
	private WorkItem aut;
	@Mock
	private EntityManager entityManager;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		aut = new WorkItem();
	}
	@Test
	public void testSetAdditionFiels() throws JAXBException{
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field());
		fields.get(0).setName("name");
		fields.get(0).setValue("value");
		String eol = System.getProperty("line.separator");  
		String expectValue = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ eol +"<xAdditionalFields xmlns=\"http://www.w3schools.com\"><xField ref=\"name\">value</xField></xAdditionalFields>";
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext = JAXBContext.newInstance("org.hcmus.tis.model.xml");
		aut.setJaxbContext(jaxbContext);
		aut.setObjectFactory(objectFactory);
		
		aut.setAdditionFiels(fields);
		
		Assert.assertEquals(expectValue, aut.getAdditionalFields());
	
	}
	@Test
	public void testGetAdditionFields() throws JAXBException{
		String eol = System.getProperty("line.separator");  
		String expectValue = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ eol +"<xAdditionalFields xmlns=\"http://www.w3schools.com\"><xField ref=\"name\">value</xField></xAdditionalFields>";
		ObjectFactory objectFactory = new ObjectFactory();
		JAXBContext jaxbContext = JAXBContext.newInstance("org.hcmus.tis.model.xml");
		aut.setJaxbContext(jaxbContext);
		aut.setObjectFactory(objectFactory);
		aut.setAdditionalFields(expectValue);
		
		List<Field> result = aut.getAdditionFields();
		
		assertEquals(1, result.size());
		assertEquals("name", result.get(0).getName());
		assertEquals("value", result.get(0).getValue());
	}

}
