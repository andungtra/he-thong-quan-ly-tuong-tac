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
public class WorkItem {
	@javax.persistence.Transient
	@Autowired
	private ObjectFactory objectFactory;
	@Autowired
	@Transient
	private WorkItemRepository workItemRepository;

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

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	@Column(updatable = false)
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date dateLastEdit;

	@ManyToOne
	private Account userLastEdit;

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

	@ManyToMany
	private Collection<MemberInformation> subcribers;

	@NotNull
	@ManyToOne
	private WorkItemStatus status;
	@OneToMany(mappedBy = "workItem", cascade = { CascadeType.PERSIST })
	private Collection<Attachment> attachments;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
	private Date dueDate;

	@PrePersist
	void prePersit() {
		this.dateCreated = new Date();
		WorkItemHistory history = new WorkItemHistory();
		history.setType(WorkItemHistoryType.created);
		writeHistory(history);
	}

	@PreRemove
	void preRemove() {
		WorkItemHistory history = new WorkItemHistory();
		history.setType(WorkItemHistoryType.deleted);
		writeHistory(history);
	}

	@PreUpdate
	void preUpdate() {
		WorkItemHistory history = new WorkItemHistory();
		WorkItem oldWorKitem = workItemRepository.findOne(this.getId());
		if (oldWorKitem.getSubcribers() == null || this.getSubcribers() == null || oldWorKitem.getSubcribers().size() == this.getSubcribers().size()) {
			history.setType(WorkItemHistoryType.updated);
			writeHistory(history);
		}

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

	private void writeHistory(WorkItemHistory history) {
		history.setChangedBy(getUserLastEdit());
		history.setWorkItem(this);
		history.setAsignee(this.asignee);
		history.setAdditionalFields(this.additionalFields);
		history.setAuthor(this.author);
		history.setDateCreated(this.dateCreated);
		history.setDateLastEdit(this.dateLastEdit);
		history.setDescription(this.description);
		history.setDueDate(this.dueDate);
		history.setPriority(this.priority);
		history.setStatus(this.status);
		history.setTitle(this.title);
		history.setWorkItemContainer(this.workItemContainer);
		history.setWorkItemType(this.workItemType);
		history.persist();
	}

	/*public static long countWorkItemByProject(Project project) {
		String jql = "SELECT COUNT(workItem) FROM WorkItem workItem WHERE workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId";
		TypedQuery<Long> query = entityManager().createQuery(jql, Long.class);
		query.setParameter("containerId", project.getId());
		long result = query.getSingleResult();
		return result;
	}

	public static long countOpenWorkItemInParent(WorkItemContainer container) {
		String jql = "SELECT COUNT(workItem) FROM WorkItem workItem WHERE (workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId) AND (workItem.status.name <> :statusName)";
		TypedQuery<Long> query = entityManager().createQuery(jql, Long.class);
		query.setParameter("containerId", container.getId());
		query.setParameter("statusName", "Closed");
		long result = query.getSingleResult();
		return result;
	}
	
	public static TypedQuery<WorkItem> findWorkItemsByProject(Project project) {
		String jql = "SELECT workItem FROM WorkItem workItem WHERE workItem.status.name <> :status AND (workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId)";
		TypedQuery<WorkItem> query = entityManager().createQuery(jql,
				WorkItem.class);
		query.setParameter("status", "Closed");
		query.setParameter("containerId", project.getId());
		return query;
	}*/

	/*public static List<WorkItem> findWorkItems(Project project,
			int iDisplayStart, int iDisplayLength, String sSearch,
			String sSearch_0, String sSearch_1, String sSearch_2,
			String sSearch_3) {
		// TODO Auto-generated method stub
		String hql = "SELECT o FROM WorkItem as o WHERE (o.workItemContainer.id =:containerId or o.workItemContainer.parentContainer.id =:containerId)";

		if (sSearch.length() > 0) {
			hql += " AND (LOWER(o.title) like LOWER(:sSearch) or LOWER(o.status.name) like LOWER(:sSearch) or LOWER(o.workItemType.name) like LOWER(:sSearch) or LOWER(o.priority.name) like LOWER(:sSearch))";

		}
		if (sSearch_0.length() > 0) {
			hql += " AND LOWER(o.title) like LOWER(:sSearch_0)";
		}
		if (sSearch_1.length() > 0) {
			hql += " AND LOWER(o.status.name) like LOWER(:sSearch_1)";
		}
		if (sSearch_2.length() > 0) {
			hql += " AND LOWER(o.workItemType.name) like LOWER(:sSearch_2)";
		}
		if (sSearch_3.length() > 0) {
			hql += " AND LOWER(o.priority.name) like LOWER(:sSearch_3)";
		}

		TypedQuery<WorkItem> query = entityManager()
				.createQuery(hql, WorkItem.class).setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength);
		if (sSearch.length() > 0)
			query.setParameter("sSearch", "%" + sSearch + "%");
		if (sSearch_0.length() > 0)
			query.setParameter("sSearch_0", "%" + sSearch_0 + "%");
		if (sSearch_1.length() > 0)
			query.setParameter("sSearch_1", "%" + sSearch_1 + "%");
		if (sSearch_2.length() > 0)
			query.setParameter("sSearch_2", "%" + sSearch_2 + "%");
		if (sSearch_3.length() > 0)
			query.setParameter("sSearch_3", "%" + sSearch_3 + "%");
		query.setParameter("containerId", project.getId());
		return query.getResultList();
	}*/

	public Collection<MemberInformation> getSubcribers() {
		return subcribers;
	}

	public void setSubcribers(Collection<MemberInformation> subcribers) {
		this.subcribers = subcribers;
	}

	/*public static TypedQuery<WorkItem> findAllWorkItemsByProject(
			Project findProject) {
		// TODO Auto-generated method stub
		String jql = "SELECT workItem FROM WorkItem workItem WHERE workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId";
		TypedQuery<WorkItem> query = entityManager().createQuery(jql,
				WorkItem.class);
		query.setParameter("containerId", findProject.getId());
		return query;
	}

	public static List listStatusByProject(Project findProject) {
		// TODO Auto-generated method stub
		String jql = "SELECT workItem.status.name, count(workItem) FROM WorkItem workItem WHERE workitem.workItemContainer.id =:containerId OR workItem.workItemContainer.parentContainer.id =:containerId GROUP BY workItem.status.name";
		Query query = entityManager().createQuery(jql);
		query.setParameter("containerId", findProject.getId());
		return query.getResultList();
	}*/

	/*public static List<WorkItem> findWorkItems(Project project,
			int iDisplayStart, int iDisplayLength, String sSearch, Long status) {
		// TODO Auto-generated method stub
		String hql = "SELECT o FROM WorkItem as o WHERE (o.workItemContainer.id =:containerId or o.workItemContainer.parentContainer.id =:containerId)";

		if (status != null && status > 0)
			hql += " AND (o.status.id =:statusId)";
		else
			hql += " AND (o.status.name <> :statusName)";
		if (sSearch.length() > 0 && !sSearch.equals("undefined")) {
			hql += " AND (LOWER(o.title) like LOWER(:sSearch) or LOWER(o.status.name) like LOWER(:sSearch) or LOWER(o.workItemType.name) like LOWER(:sSearch) or LOWER(o.priority.name) like LOWER(:sSearch))";
		}

		TypedQuery<WorkItem> query = entityManager()
				.createQuery(hql, WorkItem.class).setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength);
		if (sSearch.length() > 0 && !sSearch.equals("undefined"))
			query.setParameter("sSearch", "%" + sSearch + "%");

		query.setParameter("containerId", project.getId());

		if (status != null && status > 0)
			query.setParameter("statusId", status);
		else
			query.setParameter("statusName", "Closed");

		return query.getResultList();
	}*/

	/*public static long getTotalRecord(SearchConditionsDTO searchCondition) {
		return getFilteredRecord(null, searchCondition);
	}*/

	/*public static long getFilteredRecord(String filter,
			SearchConditionsDTO searchCondition) {
		String hql = "SELECT COUNT(workItem) FROM WorkItem workItem WHERE 1 = 1";
		if (searchCondition.getTitleDescription() != null) {
			hql = hql
					+ " AND (workItem.title LIKE :titleDescription OR workItem.description LIKE :titleDescription)";
		}

		if (searchCondition.getAsignee() != null) {
			hql = hql + " AND workItem.asignee.id =:asignee";
		}
		if (searchCondition.getOwner() != null) {
			hql = hql + " AND workItem.author.id =:author";
		}
		if (searchCondition.getStatus() != null) {
			hql = hql + " AND workItem.status.id =:status";
		}
		if (filter != null && filter.length() > 0) {
			hql = hql
					+ " AND (workItem.title LIKE :filter OR workItem.description LIKE :filter)";
		}
		ArrayList<Iteration> childContainer = null;
		if (searchCondition.getContainer() != null) {
			hql = hql + " AND (workItem.workItemContainer.id =:container";
			childContainer = new ArrayList<Iteration>(Iteration.getdescendantIterations(searchCondition.getContainer()));
			for(int index = 0; index < childContainer.size(); ++index){
				hql = hql + " OR workItem.workItemContainer.id =:container_" + String.valueOf(index);
			}
			hql = hql + ")";
		}
		EntityManager em = entityManager();
		Query query = em.createQuery(hql, Long.class);
		if (searchCondition.getTitleDescription() != null) {

			query.setParameter("titleDescription",
					"%" + searchCondition.getTitleDescription() + "%");
		}

		if (searchCondition.getAsignee() != null) {
			query.setParameter("asignee", searchCondition.getAsignee().getId());
		}
		if (searchCondition.getOwner() != null) {
			query.setParameter("author", searchCondition.getOwner().getId());
		}
		if (searchCondition.getStatus() != null) {
			query.setParameter("status", searchCondition.getStatus().getId());
		}
		if (filter != null && filter.length() > 0) {
			query.setParameter("filter", "%" + filter + "%");
		}
		if (searchCondition.getContainer() != null) {
			query.setParameter("container", searchCondition.getContainer().getId());
			for(int index = 0; index < childContainer.size(); ++index){
				query.setParameter("container_" + String.valueOf(index), childContainer.get(index).getId());
			}
		}
		return (Long) query.getSingleResult();
		return (long)1;
	}*/

	/*public static Collection<WorkItem> findWorkItem(String filter, SearchConditionsDTO searchCondition,
			int startDisplay, int displayLength) {
		String hql = "SELECT workItem FROM WorkItem workItem WHERE 1 = 1";
		if (searchCondition.getTitleDescription() != null) {
			hql = hql
					+ " AND (workItem.title LIKE :titleDescription OR workItem.description LIKE :titleDescription)";
		}

		if (searchCondition.getAsignee() != null) {
			hql = hql + " AND workItem.asignee.id =:asignee";
		}
		if (searchCondition.getOwner() != null) {
			hql = hql + " AND workItem.author.id =:author";
		}
		if (searchCondition.getStatus() != null) {
			hql = hql + " AND workItem.status.id =:status";
		}
		if (filter != null && filter.length() > 0) {
			hql = hql
					+ " AND (workItem.title LIKE :filter OR workItem.description LIKE :filter)";
		}
		ArrayList<Iteration> childContainer = null;
		if (searchCondition.getContainer() != null) {
			hql = hql + " AND (workItem.workItemContainer.id =:container";
			childContainer = new ArrayList<Iteration>(Iteration.getdescendantIterations(searchCondition.getContainer()));
			for(int index = 0; index < childContainer.size(); ++index){
				hql = hql + " OR workItem.workItemContainer.id =:container_" + String.valueOf(index);
			}
			hql = hql + ")";
		}
		EntityManager em = entityManager();
		Query query = em.createQuery(hql, WorkItem.class);
		if (searchCondition.getTitleDescription() != null) {

			query.setParameter("titleDescription",
					"%" + searchCondition.getTitleDescription() + "%");
		}

		if (searchCondition.getAsignee() != null) {
			query.setParameter("asignee", searchCondition.getAsignee().getId());
		}
		if (searchCondition.getOwner() != null) {
			query.setParameter("author", searchCondition.getOwner().getId());
		}
		if (searchCondition.getStatus() != null) {
			query.setParameter("status", searchCondition.getStatus().getId());
		}
		if (filter != null && filter.length() > 0) {
			query.setParameter("filter", "%" + filter + "%");
		}
		if (searchCondition.getContainer() != null) {
			query.setParameter("container", searchCondition.getContainer().getId());
			for(int index = 0; index < childContainer.size(); ++index){
				query.setParameter("container_" + String.valueOf(index), childContainer.get(index).getId());
			}
		}
		query.setFirstResult(startDisplay);
		query.setMaxResults(displayLength);
		return query.getResultList();
		return new HashSet<WorkItem>();
	}*/

/*	public static long countWorkItems(Project project, String sSearch,
			Long status) {
		// TODO Auto-generated method stub
		String hql = "SELECT COUNT(o) FROM WorkItem as o WHERE (o.workItemContainer.id =:containerId or o.workItemContainer.parentContainer.id =:containerId)";

		if (status != null && status > 0)
			hql += " AND (o.status.id =:statusId)";
		else
			hql += " AND (o.status.name <> :statusName)";
		if (sSearch.length() > 0 && !sSearch.equals("undefined")) {
			hql += " AND (LOWER(o.title) like LOWER(:sSearch) or LOWER(o.status.name) like LOWER(:sSearch) or LOWER(o.workItemType.name) like LOWER(:sSearch) or LOWER(o.priority.name) like LOWER(:sSearch))";
		}

		TypedQuery<Long> query = entityManager().createQuery(hql, Long.class);
		if (sSearch.length() > 0 && !sSearch.equals("undefined"))
			query.setParameter("sSearch", "%" + sSearch + "%");

		query.setParameter("containerId", project.getId());

		if (status != null && status > 0)
			query.setParameter("statusId", status);
		else
			query.setParameter("statusName", "Closed");

		return query.getSingleResult();
	}*/
}
