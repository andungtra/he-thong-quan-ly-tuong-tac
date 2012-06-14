package org.hcmus.tis.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.model.xml.XField;
import org.hcmus.tis.model.xml.XProjectProcess;
import org.hcmus.tis.model.xml.XWorkItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@Configurable
public class WorkItemType {

	@NotNull
	@Size(min = 1, max = 50)
	private String name;

	@NotNull
	private String refName;

	@NotNull
	@ManyToOne
	private ProjectProcess projectProcess;

	private String additionalFieldsDefine;

	public List<FieldDefine> getAdditionalFieldDefines() throws JAXBException {
		Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
		InputStream inputStream = new ByteArrayInputStream(this
				.getProjectProcess().getProcessTemplateFile());
		StreamSource streamSource = new StreamSource(inputStream);
		XProjectProcess xProjectProcess = unMarshaller.unmarshal(streamSource,
				XProjectProcess.class).getValue();
		List<FieldDefine> result = new ArrayList<FieldDefine>();
		XWorkItem currentXWorkItem = null;
		for (XWorkItem xWorkItem : xProjectProcess.getXWorkItems()
				.getXWorkItem()) {
			if (xWorkItem.getRefName().compareTo(refName) == 0) {
				currentXWorkItem = xWorkItem;
				break;
			}
		}
		for (XField xField : currentXWorkItem.getXAddionalFields().getXField()) {
			FieldDefine fieldDefine = new FieldDefine();
			fieldDefine.setRefName(xField.getRefName());
			fieldDefine.setName(xField.getName());
			fieldDefine.setDefaultValue(xField.getXDefaultValue());
			if (xField.isRequied() == null) {
				fieldDefine.setRequired(false);
			} else {
				fieldDefine.setRequired(xField.isRequied());
			}
			fieldDefine.setType(BuildInType.valueOf( xField.getType().toString().toLowerCase()));
			if (xField.getXChoices() != null) {
				fieldDefine.setChoices(new ArrayList<String>());
				for (String choice : xField.getXChoices().getXChoice()) {
					fieldDefine.getChoices().add(choice);
				}
			}
			result.add(fieldDefine);
		}
		return result;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	@javax.persistence.Transient
	@Autowired
	private JAXBContext jaxbContext;
}
