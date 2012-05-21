package org.hcmus.tis.model;

import java.beans.Transient;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.model.xml.ObjectFactory;
import org.hcmus.tis.model.xml.XAdditionalFieldsImpl;
import org.hcmus.tis.model.xml.XFieldImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findWorkItemsByWorkItemContainer" })
public class WorkItem {
	@javax.persistence.Transient
	@Autowired
	private ObjectFactory objectFactory;
	public void setAdditionFiels(List<Field> additionalFields) throws JAXBException{
		XAdditionalFieldsImpl xAdditionalFields = getObjectFactory().createXAdditionalFieldsImpl();
		for(Field field : additionalFields){
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
	public List<Field> getAdditionFields() throws JAXBException{
		List<Field> additionFiels = new ArrayList<Field>();
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(this.getAdditionalFields());
		Source source = new StreamSource(reader);
		XAdditionalFieldsImpl additionalFieldsImpl = unmarshaller.unmarshal(source, XAdditionalFieldsImpl.class).getValue();
		for(XFieldImpl xFieldImpl : additionalFieldsImpl.getXField()){
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

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateCreated;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateLastEdit;
        
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

    @NotNull
    @ManyToOne
    private MemberInformation author;

    @ManyToOne
    private MemberInformation asignee;

    @NotNull
    @ManyToOne
    private WorkItemStatus status;
    @OneToMany(mappedBy="workItem", cascade={CascadeType.PERSIST})
    private Collection<Attachment> attachments;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style =  "SS")
    private Date dueDate;

    @PrePersist
    void prePersit() {    	
        this.dateCreated = new Date();
        WorkItemHistory history = new WorkItemHistory();
    	history.setType(WorkItemHistoryType.CREATE);
    	writeHistory(history);
    }
    
    @PreRemove
    void preRemove(){
    	WorkItemHistory history = new WorkItemHistory();
    	history.setType(WorkItemHistoryType.DELETE);
    	writeHistory(history);
    }
    
    @PreUpdate
    void preUpdate(){
    	WorkItemHistory history = new WorkItemHistory();
    	history.setType(WorkItemHistoryType.UPDATE);
    	writeHistory(history);
    }
    
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
	public Collection<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Collection<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	private void writeHistory(WorkItemHistory history){
		
    	history.setWorkItem(this);
    	history.setAsignee(this.asignee);
    	history.setAdditionalFields(this.additionalFields);
    	history.setAuthor(this.author);
    	history.setDateCreated(this.dateCreated);
    	history.setDateLastEdit(this.dateLastEdit);
    	history.setDescription(this.description);
    	history.setDueDate(this.dueDate);
    	history.setPriority(this.priority) ;
    	history.setStatus(this.status);
    	history.setTitle(this.title);
    	history.setWorkItemContainer(this.workItemContainer);
    	history.setWorkItemType(this.workItemType);
    	history.persist();
	}
	public static long countWorkItemByProject(Project project){
		String jql ="SELECT COUNT(workItem) FROM WorkItem workItem WHERE workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId";
		TypedQuery<Long> query = entityManager().createQuery(jql, Long.class);
		query.setParameter("containerId", project.getId());
		long result = query.getSingleResult();
		return result;
	}
	public static TypedQuery<WorkItem> findWorkItemsByProject(Project project){
		String jql ="SELECT workItem FROM WorkItem workItem WHERE workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId";
		TypedQuery<WorkItem> query = entityManager().createQuery(jql, WorkItem.class);
		query.setParameter("containerId", project.getId());
		return query;
	}
}
