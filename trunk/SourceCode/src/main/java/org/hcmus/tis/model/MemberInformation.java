package org.hcmus.tis.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooJpaActiveRecord(finders = { "findMemberInformationsByAccountAndProject" })
public class MemberInformation {

    @ManyToOne
    @NotNull
    private Account account;

    @ManyToOne
    @NotNull
    private Project project;

    @ManyToOne
    @NotNull
    private MemberRole memberRole;

    @Value("false")
    private Boolean deleted;

    public String toString() {
        return account.getEmail();
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) {
            this.entityManager = entityManager();
        }
        if (this.getProject() != null) {
            this.getProject().getMemberInformations().remove(this);
        }
        if (this.entityManager.contains(this)) {
            this.setDeleted(true);
        } else {
            MemberInformation attached = MemberInformation.findMemberInformation(this.getId());
            attached.setDeleted(true);
        }
    }

	public static List<MemberInformation> findMemberInformationsByProject(Project project) {
		// TODO Auto-generated method stub
        if (project == null) throw new IllegalArgumentException("The project argument is required");
        EntityManager em = MemberInformation.entityManager();
        TypedQuery<MemberInformation> q = em.createQuery("SELECT o FROM MemberInformation AS o WHERE o.project = :project", MemberInformation.class);
      
        q.setParameter("project", project);
        return q.getResultList();
	
	}

	public static List<MemberInformation> findAllMemberInformationsByAccount(Account account) {
		// TODO Auto-generated method stub
		if (account == null) throw new IllegalArgumentException("The account argument is required");
        EntityManager em = MemberInformation.entityManager();
        TypedQuery<MemberInformation> q = em.createQuery("SELECT o FROM MemberInformation AS o WHERE o.account = :account", MemberInformation.class);
      
        q.setParameter("account", account);
        return q.getResultList();
	}

	public static List<MemberInformation> findMemberInformationEntries(
			int iDisplayStart, int iDisplayLength, String sSearch, String sSearch_0, String sSearch_1) {
		// TODO Auto-generated method stub
		String hql = "SELECT o FROM MemberInformation AS o";
		int h =-1;
		if(sSearch.length()>0){			
			hql+= " WHERE LOWER(o.account.firstName) like LOWER(:sSearch) or LOWER(o.account.lastName) like LOWER(:sSearch)";
			h=1;
		}
		if(sSearch_0.length()>0){
			if(h==1) hql += " AND";
			else hql += " WHERE";			
			hql += " LOWER(o.account.firstName) like LOWER(:sSearch_0)";
			h=1;
		}
		if(sSearch_1.length()>0){
			if(h==1) hql += " AND";
			else hql += " WHERE";
			hql += " LOWER(o.account.lastName) like LOWER(:sSearch_1)";
		}
			
		EntityManager em = MemberInformation.entityManager();
        TypedQuery<MemberInformation> q = em.createQuery(hql , MemberInformation.class);
        if(sSearch.length()>0)
        	q.setParameter("sSearch", "%"+sSearch+"%");
        if(sSearch_0.length()>0)
        	q.setParameter("sSearch_0", "%"+sSearch_0+"%");
        if(sSearch_1.length()>0)
        	q.setParameter("sSearch_1", "%"+sSearch_1+"%");
        return q.getResultList();
	}

	public static List<MemberInformation> findMemberInformationEntriesBaseProject(
			int iDisplayStart, int iDisplayLength, String sSearch,
			String sSearch_0, String sSearch_1) {
		// TODO Auto-generated method stub
		String hql = "SELECT o FROM MemberInformation AS o";
		int h =-1;
		if(sSearch.length()>0){			
			hql+= " WHERE (LOWER(o.project.name) like LOWER(:sSearch) or LOWER(o.project.description) like LOWER(:sSearch))";
			h=1;
		}
		if(sSearch_0.length()>0){
			if(h==1) hql += " AND";
			else hql += " WHERE";			
			hql += " LOWER(o.project.name) like LOWER(:sSearch_0)";
			h=1;
		}
		if(sSearch_1.length()>0){
			if(h==1) hql += " AND";
			else hql += " WHERE";
			hql += " LOWER(o.project.description) like LOWER(:sSearch_1)";
		}
			
		EntityManager em = MemberInformation.entityManager();
        TypedQuery<MemberInformation> q = em.createQuery(hql , MemberInformation.class);
        if(sSearch.length()>0)
        	q.setParameter("sSearch", "%"+sSearch+"%");
        if(sSearch_0.length()>0)
        	q.setParameter("sSearch_0", "%"+sSearch_0+"%");
        if(sSearch_1.length()>0)
        	q.setParameter("sSearch_1", "%"+sSearch_1+"%");
        return q.getResultList();
	}

	public static List<MemberInformation> findMemberInformationsByProjectBaseAccount(
			Project findProject,int iDisplayStart, int iDisplayLength, String sSearch, String sSearch_0,
			String sSearch_1, String sSearch_2) {
		// TODO Auto-generated method stub
		String hql = "SELECT o FROM MemberInformation AS o WHERE o.project = :project";
		
		if(sSearch.length()>0){			
			hql+= " AND (LOWER(o.account.firstName) like LOWER(:sSearch) or LOWER(o.account.lastName) like LOWER(:sSearch) or LOWER(o.memberRole.name) like LOWER(:sSearch))";			
		}
		if(sSearch_0.length()>0){					
			hql += " AND LOWER(o.account.firstName) like LOWER(:sSearch_0)";			
		}
		if(sSearch_1.length()>0){					
			hql += " AND LOWER(o.account.lastName) like LOWER(:sSearch_1)";			
		}
		if(sSearch_2.length()>0){			
			hql += " AND LOWER(o.memberRole.name) like LOWER(:sSearch_2)";
		}
		
		EntityManager em = MemberInformation.entityManager();
        TypedQuery<MemberInformation> q = em.createQuery(hql , MemberInformation.class);
        if(sSearch.length()>0)
        	q.setParameter("sSearch", "%"+sSearch+"%");
        if(sSearch_0.length()>0)
        	q.setParameter("sSearch_0", "%"+sSearch_0+"%");
        if(sSearch_1.length()>0)
        	q.setParameter("sSearch_1", "%"+sSearch_1+"%");
        if(sSearch_2.length()>0)
        	q.setParameter("sSearch_2", "%"+sSearch_2+"%");
        q.setParameter("project", findProject);
        return q.getResultList();
	}
}
