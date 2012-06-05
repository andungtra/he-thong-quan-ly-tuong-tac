package org.hcmus.tis.model.unittest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

import org.hcmus.tis.model.Field;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.xml.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class WorkItemTest {
	private WorkItem aut;
	@Before
	public void setUp(){
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
