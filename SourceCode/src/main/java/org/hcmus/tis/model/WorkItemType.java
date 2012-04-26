package org.hcmus.tis.model;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.model.xml.XAdditionalFieldsImpl;
import org.hcmus.tis.model.xml.XFieldImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class WorkItemType {

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    @ManyToOne
    private ProjectProcess projectProcess;

    private String additionalFieldsDefine;
    
	public List<FieldDefine> getAdditionalFieldDefines() throws JAXBException{
		Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(additionalFieldsDefine);
		StreamSource streamSource = new StreamSource(stringReader);
		XAdditionalFieldsImpl xAdditionalFields =  unMarshaller.unmarshal(streamSource, XAdditionalFieldsImpl.class).getValue();
		List<FieldDefine> result = new ArrayList<FieldDefine>();
		for(XFieldImpl xFieldImpl : xAdditionalFields.getXField()){
			FieldDefine fieldDefine = new FieldDefine();
			fieldDefine.setRefName(xFieldImpl.getRef());
			result.add(fieldDefine);
		}
		return result;
	}
	@javax.persistence.Transient
	@Autowired
	private JAXBContext jaxbContext;
}
