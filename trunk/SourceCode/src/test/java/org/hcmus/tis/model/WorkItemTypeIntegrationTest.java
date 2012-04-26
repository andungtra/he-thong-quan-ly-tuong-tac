package org.hcmus.tis.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
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

import org.junit.Before;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@RooIntegrationTest(entity = WorkItemType.class)
public class WorkItemTypeIntegrationTest {
	private WorkItemType aut;
	@Before
	public void setUp(){
		aut = new WorkItemType();
	}
    @Test
    public void testMarkerMethod() {
    }
	@Test
	public void testGetAdditionalFieldsArray() throws ParserConfigurationException, SAXException, IOException, TransformerException, JAXBException {
		File xmlFile = new File("src/test/java/AdditionalFieldsImpl.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(xmlFile);
		 DOMSource domSource = new DOMSource(document);
		 StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       String xmlString = writer.toString();
	       aut.setAdditionalFieldsDefine(xmlString);
	       JAXBContext jaxbContext = JAXBContext.newInstance("org.hcmus.tis.model.xml");
	       aut.setJaxbContext(jaxbContext);
	       List<FieldDefine> additionalFiels = aut.getAdditionalFieldDefines();
	       assertEquals(3, additionalFiels.size());
	       assertEquals("difficult", additionalFiels.get(1).getRefName());
	}
}
