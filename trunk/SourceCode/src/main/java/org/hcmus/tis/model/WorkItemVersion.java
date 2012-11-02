package org.hcmus.tis.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.model.xml.ObjectFactory;
import org.hcmus.tis.model.xml.XAdditionalFieldsImpl;
import org.hcmus.tis.model.xml.XFieldImpl;
import org.hcmus.tis.repository.WorkItemHistoryRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hibernate.mapping.Array;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@Configurable
public class WorkItemVersion {
	@javax.persistence.Transient
	@Autowired
	private ObjectFactory objectFactory;

	public void setAdditionFiels(List<Field> additionalFields)
			throws JAXBException {
		XAdditionalFieldsImpl xAdditionalFields = getObjectFactory()
				.createXAdditionalFieldsImpl();
		for (Field field : additionalFields) {
			XFieldImpl xField = getObjectFactory().createXFieldImpl();
			xField.setRef(field.getName());
			xField.setContent(field.getValue());
			xAdditionalFields.getXField().add(xField);
		}
		Marshaller marshaller = jaxbContext.createMarshaller();
		Writer writer = new StringWriter();
		marshaller.marshal(xAdditionalFields, writer);
		this.additionalFields = writer.toString();
	}

	public List<Field> getAdditionFields() throws JAXBException {
		List<Field> additionFiels = new ArrayList<Field>();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(this.getAdditionalFields());
		Source source = new StreamSource(reader);
		XAdditionalFieldsImpl additionalFieldsImpl = unmarshaller.unmarshal(
				source, XAdditionalFieldsImpl.class).getValue();
		for (XFieldImpl xFieldImpl : additionalFieldsImpl.getXField()) {
			Field field = new Field();
			field.setName(xFieldImpl.getRef());
			field.setValue(xFieldImpl.getContent());
			additionFiels.add(field);
		}
		return additionFiels;
	}

	@javax.persistence.Transient
	@Autowired
	private JAXBContext jaxbContext;
	@NotNull
	@Size(min = 1, max = 50)
	private String title;

	private String description;

	private String additionalFields;

	@NotNull
	@ManyToOne
	private Priority priority;

	@NotNull
	@ManyToOne
	private WorkItemContainer workItemContainer;

	@NotNull
	@ManyToOne
	private WorkItemType workItemType;

	@ManyToOne
	private MemberInformation asignee;

	@NotNull
	@ManyToOne
	private WorkItemStatus status;
	@OneToMany(mappedBy = "workItem", cascade = { CascadeType.PERSIST })
	private Collection<Attachment> attachments;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date dueDate;

	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}

	public Collection<Attachment> getAttachments() {
		return attachments;
	}
	@NotNull
	@ManyToOne
	private WorkItem workItem;

	public WorkItem getWorkItem() {
		return workItem;
	}

	public void setWorkItem(WorkItem workItem) {
		this.workItem = workItem;
	}

	public WorkItemVersion clone(){
		WorkItemVersion clonedWorkItem = new WorkItemVersion();
		clonedWorkItem.additionalFields = this.additionalFields;
		clonedWorkItem.asignee = this.asignee;
		clonedWorkItem.attachments = this.attachments;
		clonedWorkItem.description = this.description;
		clonedWorkItem.dueDate = this.dueDate;
		clonedWorkItem.priority = this.priority;
		clonedWorkItem.status = this.status;
		clonedWorkItem.title = this.title;
		clonedWorkItem.workItemContainer = this.workItemContainer;
		clonedWorkItem.workItemType = this.workItemType;
		clonedWorkItem.setId(this.getId());
		clonedWorkItem.setVersion(this.getVersion());
		return clonedWorkItem;
	}


}
