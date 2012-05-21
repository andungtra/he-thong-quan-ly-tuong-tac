package org.hcmus.tis.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
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
@RooJpaActiveRecord(finders = { "findStudyClassesByNameLike" })
public class StudyClass {

    @NotNull
    @Size(max = 50, min = 1)
    private String name;

    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Project> projects = new HashSet<Project>();
    
    @Value("false")
    private boolean isDeleted;

	public static List<StudyClass> findStudyClassEntries(int iDisplayStart,
			int iDisplayLength, String sSearch) {
		// TODO Auto-generated method stub
		if(sSearch.length()==0)
			return findStudyClassEntries(iDisplayStart, iDisplayLength);
		
		String queryString = "SELECT s FROM StudyClass AS s WHERE s.name like :sSearch";
        TypedQuery<StudyClass> query = entityManager().createQuery(queryString, StudyClass.class).setFirstResult(iDisplayStart).setMaxResults(iDisplayLength);
        query.setParameter("sSearch", "%"+sSearch+"%");
        return query.getResultList();
	}
}
