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
    @Basic(fetch=FetchType.LAZY)
    @NotNull
    private byte[] processTemplateFile;

    @NotNull
    @Column(unique = true)
    private String uniqueName;

    @OneToMany(mappedBy = "projectProcess", cascade = CascadeType.ALL)
    private List<WorkItemType> workItemTypes;

    @Value("false")
    private boolean isDeleted;

	public static List<ProjectProcess> findProjectProcessEntries(
			int iDisplayStart, int iDisplayLength, String sSearch, String sSearch_0, String sSearch_1) {
		// TODO Auto-generated method stub
		String hql =  "SELECT o FROM ProjectProcess AS o";
	
		int h=-1;
		if(sSearch.length()>0){
			hql += " WHERE (LOWER(o.name) LIKE LOWER(:sSearch) or LOWER(o.description) LIKE LOWER(:sSearch))";
			h =1;
		}
		if(sSearch_0.length()>0){
			if(h==1)
				hql += " AND";
			else
				hql += " WHERE";
			h=1;
			hql += " LOWER(o.name) LIKE LOWER(:sSearch_0)";
		}
		if(sSearch_1.length()>0){
			if(h==1)
				hql += " AND";
			else
				hql += " WHERE";
			h=1;
			hql += " LOWER(o.description) LIKE LOWER(:sSearch_1)";
		}
		EntityManager entityManager = Project.entityManager();
		
		TypedQuery<ProjectProcess> q = entityManager.createQuery(hql, ProjectProcess.class);
		if(sSearch.length()>0)
			q.setParameter("sSearch", "%"+sSearch+"%");
		if(sSearch_0.length()>0)
			q.setParameter("sSearch_0", "%"+sSearch_0+"%");
		if(sSearch_1.length()>0)
			q.setParameter("sSearch_1", "%"+sSearch_1+"%");
		return q.getResultList();
	}
}
