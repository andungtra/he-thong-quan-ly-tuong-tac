package org.hcmus.tis.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findStudyClassesByNameLike" })
public class StudyClass {

	@NotNull
	@Size(max = 50, min = 1)
	private String name;

	private String description;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy="studyClass")
	private Set<Project> projects = new HashSet<Project>();

	@Value("false")
	private boolean deleted;

	public static List<StudyClass> findStudyClassEntries(int iDisplayStart,
			int iDisplayLength, String sSearch, String sSearch_0,
			String sSearch_1) {
		// TODO Auto-generated method stub

		String hql = "SELECT s FROM StudyClass AS s";
		int h = -1;
		if (sSearch.length() > 0) {
			hql += " WHERE (LOWER(s.name) like LOWER(:sSearch) or LOWER(s.description) like LOWER(:sSearch))";
			h = 1;
		}
		if (sSearch_0.length() > 0) {
			if (h == 1)
				hql += " AND";
			else
				hql += " WHERE";
			h = 1;
			hql += " LOWER(s.name) like LOWER(:sSearch_0)";
		}

		if (sSearch_1.length() > 0) {
			if (h == 1)
				hql += " AND";
			else
				hql += " WHERE";
			hql += " LOWER(s.description) like LOWER(:sSearch_1)";
		}
		TypedQuery<StudyClass> query = entityManager()
				.createQuery(hql, StudyClass.class)
				.setFirstResult(iDisplayStart).setMaxResults(iDisplayLength);
		if (sSearch.length() > 0)
			query.setParameter("sSearch", "%" + sSearch + "%");
		if (sSearch_0.length() > 0)
			query.setParameter("sSearch_0", "%" + sSearch_0 + "%");
		if (sSearch_1.length() > 0)
			query.setParameter("sSearch_1", "%" + sSearch_1 + "%");
		return query.getResultList();
	}

	public static long countStudyClassesNotDeleted() {
		// TODO Auto-generated method stub
		EntityManager entityManager = Project.entityManager();
		String jpq = "SELECT COUNT(o) FROM StudyClass o WHERE o.isDeleted <> :isDeleted";
		TypedQuery<Long> q = entityManager.createQuery(jpq, Long.class);
		q.setParameter("isDeleted", true);
		return q.getSingleResult();
	}

	public static List<StudyClass> findStudyClassEntries(int iDisplayStart,
			int iDisplayLength, String sSearch) {
		// TODO Auto-generated method stub
		String hql = "SELECT s FROM StudyClass AS s WHERE (s.isDeleted = :isDeleted)";

		if (sSearch.length() > 0) {
			hql += " AND (LOWER(s.name) like LOWER(:sSearch) or LOWER(s.description) like LOWER(:sSearch))";
		}

		TypedQuery<StudyClass> query = entityManager()
				.createQuery(hql, StudyClass.class)
				.setFirstResult(iDisplayStart).setMaxResults(iDisplayLength);
		if (sSearch.length() > 0)
			query.setParameter("sSearch", "%" + sSearch + "%");
		query.setParameter("isDeleted", false);
		return query.getResultList();
	}

	public static long StudyClassEntries(String sSearch) {
		// TODO Auto-generated method stub
		String hql = "SELECT COUNT(s) FROM StudyClass AS s WHERE (s.isDeleted = :isDeleted)";

		if (sSearch.length() > 0) {
			hql += " AND (LOWER(s.name) like LOWER(:sSearch) or LOWER(s.description) like LOWER(:sSearch))";
		}

		TypedQuery<Long> query = entityManager().createQuery(hql, Long.class);
		if (sSearch.length() > 0)
			query.setParameter("sSearch", "%" + sSearch + "%");
		query.setParameter("isDeleted", false);
		return query.getSingleResult();
	}
}
