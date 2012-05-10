package org.hcmus.tis.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.hcmus.tis.dto.NonEditableEvent;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findProjectsByNameLike" })
public class Project extends WorkItemContainer {
	private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "project")
    private Set<MemberInformation> memberInformations = new HashSet<MemberInformation>();

    @ManyToOne
    private StudyClass studyClass;
    @ManyToOne
    @NotNull
    private ProjectProcess projectProcess;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(nullable=false, updatable=false)
    private Calendar calendar;
    @PrePersist
    public void prePersit(){
    	calendar = new Calendar();
    }
    @Enumerated
    private ProjectStatus status;
    
    public static long countProjectsByNameLike(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
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

    public static TypedQuery<org.hcmus.tis.model.Project> findProjectsByNameLike(String name, int firstIndex, int maxSize) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = Project.entityManager();
        TypedQuery<Project> q = em.createQuery("SELECT o FROM Project AS o WHERE LOWER(o.name) LIKE LOWER(:name)", Project.class).setFirstResult(firstIndex).setMaxResults(maxSize);
        q.setParameter("name", name);
        return q;    
    }
    
    public static Collection<org.hcmus.tis.model.Project> findProjectsByAccount(long AccountID) {
        EntityManager em = Project.entityManager();
        TypedQuery<MemberInformation> q = em.createQuery("SELECT o FROM MemberInformation AS o WHERE o.account.id = (:id)",MemberInformation.class);
        q.setParameter("id", AccountID);
        Collection<MemberInformation> listID = q.getResultList();
        ArrayList<org.hcmus.tis.model.Project> rs = new ArrayList<Project>();
        for (MemberInformation info : listID) {			
			rs.add(info.getProject());
		}
        return rs;
    }
    public Project getParentProjectOrMyself(){
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
	public Collection<Event> getEventsOfMembers(){
		EntityManager entityManager = Project.entityManager();
		String jpq = "SELECT DISTINCT event FROM Project project JOIN project.memberInformations memberInformation JOIN memberInformation.account.calendar calendar JOIN calendar.events event WHERE project.id = :projectId  AND event NOT MEMBER OF project.calendar.events";
		TypedQuery<Event> q = entityManager.createQuery(jpq, Event.class);
		q.setParameter("projectId", this.getId());
	
		return q.getResultList();
	}
}
