package org.hcmus.tis.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ProjectProcess {

	@NotNull
	@Size(max = 50, min = 1)
	private String name;

	private String description;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@NotNull
	@Column(updatable=false)
	private byte[] processTemplateFile;

	@NotNull
	@Column(unique = true)
	private String uniqueName;

	@OneToMany(mappedBy = "projectProcess", cascade = CascadeType.ALL)
	private List<WorkItemType> workItemTypes;

	@Value("false")
	private boolean isDeleted;

	public static List<ProjectProcess> findProjectProcessEntries(
			int iDisplayStart, int iDisplayLength, String sSearch) {
		// TODO Auto-generated method stub
		String hql = "SELECT o FROM ProjectProcess AS o";

		if (sSearch.length() > 0) {
			hql += " WHERE (LOWER(o.name) LIKE LOWER(:sSearch) or LOWER(o.description) LIKE LOWER(:sSearch))";

		}

		EntityManager entityManager = entityManager();

		TypedQuery<ProjectProcess> q = entityManager
				.createQuery(hql, ProjectProcess.class)
				.setFirstResult(iDisplayStart).setMaxResults(iDisplayLength);
		if (sSearch.length() > 0)
			q.setParameter("sSearch", "%" + sSearch + "%");

		return q.getResultList();
	}

	public static long countProjectProcessEntries(String sSearch) {
		// TODO Auto-generated method stub
		String hql = "SELECT COUNT(o) FROM ProjectProcess AS o";

		if (sSearch.length() > 0) {
			hql += " WHERE (LOWER(o.name) LIKE LOWER(:sSearch) or LOWER(o.description) LIKE LOWER(:sSearch))";

		}

		EntityManager entityManager = entityManager();

		TypedQuery<Long> q = entityManager.createQuery(hql, Long.class);
		if (sSearch.length() > 0)
			q.setParameter("sSearch", "%" + sSearch + "%");

		return q.getSingleResult();
	}

}
