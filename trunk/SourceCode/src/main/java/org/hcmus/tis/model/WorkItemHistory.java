package org.hcmus.tis.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public class WorkItemHistory {
	
	@ManyToOne
	private WorkItem workItem;
	
	@ManyToOne
	private Account changedBy;
	
	private WorkItemHistoryType type;
	
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
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dateCreated;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
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

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dueDate;

    @PrePersist
    void prePersit() {
        this.dateCreated = new Date();
    }
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
	
	public static List<WorkItemHistory> findAllWorkItemHistorysInProject(Long id, int maxResults) {
		// TODO Auto-generated method stub
		return entityManager().createQuery(String.format("SELECT o FROM WorkItemHistory o where o.workItemContainer.id = %d ORDER BY o.dateLastEdit DESC", id), WorkItemHistory.class).setMaxResults(maxResults).getResultList();
	}
	public static List<WorkItemHistory> findAllWorkItemHistorysOfWorkItem(
			Long workItemId, int maxResults) {
		// TODO Auto-generated method stub
		return entityManager().createQuery(String.format("SELECT o FROM WorkItemHistory o where o.workItem.id = %d ORDER BY o.dateLastEdit DESC", workItemId), WorkItemHistory.class).setMaxResults(maxResults).getResultList();
	}
}
