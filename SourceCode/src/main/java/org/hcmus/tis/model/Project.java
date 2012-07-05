package org.hcmus.tis.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.hcmus.tis.repository.EventRepository;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaEntity
@Configurable
public class Project extends WorkItemContainer {
	@Autowired
	@Transient
	EventRepository eventRepository;
	@Type(type="text")
	private String description;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "project")
	private Set<MemberInformation> memberInformations = new HashSet<MemberInformation>();

	@ManyToOne
	private StudyClass studyClass;
	@ManyToOne
	@NotNull
	@JoinColumn(updatable = false)
	private ProjectProcess projectProcess;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, updatable = false)
	private Calendar calendar;

	@PrePersist
	public void prePersit() {
		calendar = new Calendar();
	}
	@NotNull
	@Enumerated
	private ProjectStatus status;

/*	public static long countProjectsByNameLike(String name) {
		if (name == null || name.length() == 0)
			throw new IllegalArgumentException("The name argument is required");
		name = name.replace('*', '%');
		if (name.charAt(0) != '%') {
			name = "%" + name;
		}
		if (name.charAt(name.length() - 1) != '%') {
			name = name + "%";
		}
		String query = "SELECT count(*) FROM Project AS o WHERE LOWER(o.name) LIKE LOWER(:name)";
		EntityManager em = Project.entityManager();
		Query q = em.createQuery(query);
		q.setParameter("name", name);
		return (Long) q.getSingleResult();
	}

	public static TypedQuery<org.hcmus.tis.model.Project> findProjectsByNameLike(
			String name, int firstIndex, int maxSize) {
		if (name == null || name.length() == 0)
			throw new IllegalArgumentException("The name argument is required");
		name = name.replace('*', '%');
		if (name.charAt(0) != '%') {
			name = "%" + name;
		}
		if (name.charAt(name.length() - 1) != '%') {
			name = name + "%";
		}
		EntityManager em = Project.entityManager();
		TypedQuery<Project> q = em
				.createQuery(
						"SELECT o FROM Project AS o WHERE LOWER(o.name) LIKE LOWER(:name)",
						Project.class).setFirstResult(firstIndex)
				.setMaxResults(maxSize);
		q.setParameter("name", name);
		return q;
	}

	public static Collection<org.hcmus.tis.model.Project> findProjectsByAccount(
			long AccountID) {
		EntityManager em = Project.entityManager();
		TypedQuery<MemberInformation> q = em
				.createQuery(
						"SELECT o FROM MemberInformation AS o WHERE o.account.id = (:id)",
						MemberInformation.class);
		q.setParameter("id", AccountID);
		Collection<MemberInformation> listID = q.getResultList();
		ArrayList<org.hcmus.tis.model.Project> rs = new ArrayList<Project>();
		for (MemberInformation info : listID) {
			rs.add(info.getProject());
		}
		return rs;
	}*/

	public Project getParentProjectOrMyself() {
		return this;
	}

	public ProjectProcess getProjectProcess() {
		return projectProcess;
	}

	public void setProjectProcess(ProjectProcess projectProcess) {
		this.projectProcess = projectProcess;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	protected void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Collection<Event> getEventsOfMembers() {
		/*EntityManager entityManager = Project.entityManager();
		String jpq = "SELECT DISTINCT event FROM Project project JOIN project.memberInformations memberInformation JOIN memberInformation.account.calendar calendar JOIN calendar.events event WHERE project.id = :projectId AND memberInformation.deleted = false  AND event NOT MEMBER OF project.calendar.events";
		TypedQuery<Event> q = entityManager.createQuery(jpq, Event.class);
		q.setParameter("projectId", this.getId());

		return q.getResultList();*/
		return eventRepository.findEventsOfMembers(this);
	}

	/*public static List<Project> findProjectEntries(int iDisplayStart,
			int iDisplayLength, String sSearch, String sSearch_0,
			String sSearch_1, String sSearch_2) {
		// TODO Auto-generated method stub

		EntityManager entityManager = Project.entityManager();

		String jpq = "SELECT o FROM Project AS o";
		int h = -1;
		if (sSearch.length() > 0) {
			jpq += " WHERE (LOWER(o.name) LIKE LOWER(:sSearch) OR LOWER(o.description) LIKE LOWER(:sSearch) OR LOWER(o.parentContainer.name) LIKE LOWER(:sSearch))";
			h = 1;
		}

		if (sSearch_0.length() > 0) {
			if (h == 1)
				jpq += " AND";
			else
				jpq += " WHERE";

			jpq += " LOWER(o.name) LIKE LOWER(:sSearch_0)";
			h = 1;
		}
		if (sSearch_1.length() > 0) {
			if (h == 1)
				jpq += " AND";
			else
				jpq += " WHERE";

			jpq += " LOWER(o.parentContainer.name) LIKE LOWER(:sSearch_1)";
			h = 1;
		}
		if (sSearch_2.length() > 0) {
			if (h == 1)
				jpq += " AND";
			else
				jpq += " WHERE";
			jpq += " LOWER(o.description) LIKE LOWER(:sSearch_2)";
		}

		TypedQuery<Project> q = entityManager.createQuery(jpq, Project.class);
		if (sSearch.length() > 0)
			q.setParameter("sSearch", "%" + sSearch + "%");
		if (sSearch_0.length() > 0)
			q.setParameter("sSearch_0", "%" + sSearch_0 + "%");
		if (sSearch_1.length() > 0)
			q.setParameter("sSearch_1", "%" + sSearch_1 + "%");
		if (sSearch_2.length() > 0)
			q.setParameter("sSearch_2", "%" + sSearch_2 + "%");

		return q.getResultList();
	}

	public static long countAllProjectsNotDeleted() {
		// TODO Auto-generated method stub
		EntityManager entityManager = Project.entityManager();
		String jpq = "SELECT COUNT(o) FROM Project AS o WHERE (o.status IS NULL or  o.status <> :status)";
		TypedQuery<Long> q = entityManager.createQuery(jpq, Long.class);
		q.setParameter("status", ProjectStatus.DELETED);
		return q.getSingleResult();
	}

	public static List<Project> findProjectEntries(int iDisplayStart,
			int iDisplayLength, String sSearch) {
		// TODO Auto-generated method stub
		EntityManager entityManager = Project.entityManager();

		String jpq = "SELECT o FROM Project AS o WHERE (o.status IS NULL or  o.status <> :status)";

		if (sSearch.length() > 0 && !sSearch.equals("undefined")) {
			jpq += " AND (LOWER(o.name) LIKE LOWER(:sSearch) OR LOWER(o.description) LIKE LOWER(:sSearch) OR LOWER(o.parentContainer.name) LIKE LOWER(:sSearch))";
		}

		TypedQuery<Project> q = entityManager.createQuery(jpq, Project.class).setFirstResult(iDisplayStart).setMaxResults(iDisplayLength);
		if (sSearch.length() > 0 && !sSearch.equals("undefined"))
			q.setParameter("sSearch", "%" + sSearch + "%");
		
		q.setParameter("status", ProjectStatus.DELETED);

		return q.getResultList();
	}

	public static long countProjectEntries(String sSearch) {
		// TODO Auto-generated method stub
		EntityManager entityManager = Project.entityManager();

		String jpq = "SELECT COUNT(o) FROM Project AS o WHERE (o.status IS NULL or  o.status <> :status)";

		if (sSearch.length() > 0 && !sSearch.equals("undefined")) {
			jpq += " AND (LOWER(o.name) LIKE LOWER(:sSearch) OR LOWER(o.description) LIKE LOWER(:sSearch) OR LOWER(o.parentContainer.name) LIKE LOWER(:sSearch))";
		}

		TypedQuery<Long> q = entityManager.createQuery(jpq, Long.class);
		if (sSearch.length() > 0 && !sSearch.equals("undefined"))
			q.setParameter("sSearch", "%" + sSearch + "%");
		
		q.setParameter("status", ProjectStatus.DELETED);

		return q.getSingleResult();
	}*/
}
