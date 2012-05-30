package org.hcmus.tis.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findAccountsByEmailEquals" })
public class Account implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String email;

    @NotNull
    @Size(min = 1, max = 50)
    private String password;

    @NotNull
    @Value("false")
    private Boolean isAdmin;

    @Value("false")
    private Boolean isEnable;

    @Enumerated
    @NotNull
    private AccountStatus status;
    
    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(updatable=false)
    private Calendar calendar;
    @PrePersist
    public void prePersit(){
    	setCalendar(new Calendar());
    }
    public String toString() {
        return email;
    }

    public static Collection<org.hcmus.tis.model.Account> findAccount(String email, int firstIndex, int maxResult) {
        email = "%" + email + "%";
        String queryString = "SELECT account FROM Account AS account WHERE account.email LIKE :email";
        TypedQuery<Account> query = entityManager().createQuery(queryString, Account.class);
        query.setParameter("email", email);
        query.setFirstResult(firstIndex);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    public static org.hcmus.tis.model.Account getAccountbyEmail(String email) {
        String queryString = "SELECT account FROM Account AS account WHERE account.email = :email";
        TypedQuery<Account> query = entityManager().createQuery(queryString, Account.class);
        query.setParameter("email", email);
        
        return query.getSingleResult();        
    }
	public static List<Account> findAccountEntries(int iDisplayStart,
			int iDisplayLength, String sSearch, String sSearch_0, String sSearch_1, String sSearch_2, String sSearch_3) {
		// TODO Auto-generated method stub		
		
		String hql = "SELECT account FROM Account AS account";
		int h =-1;
		if(sSearch.length()>0){
			hql += " WHERE (LOWER(account.firstName) like LOWER(:sSearch) or LOWER(account.lastName) like LOWER(:sSearch) or LOWER(account.email) like LOWER(:sSearch) or LOWER(account.status) like LOWER(:sSearch))";
			h=1;
		}			
		if(sSearch_0.length()>0){
			if(h==1)
				hql+= " AND";
			else hql += " WHERE";
			h=1;
			hql += " LOWER(account.firstName) like LOWER(:sSearch_0)";			
		}
		if(sSearch_1.length()>0){
			if(h==1)
				hql+= " AND";
			else hql += " WHERE";
			h=1;
			hql += " LOWER(account.lastName) like LOWER(:sSearch_1)";
		}			
		if(sSearch_2.length()>0){
			if(h==1)
				hql+= " AND";
			else hql += " WHERE";
			h=1;
			hql += " LOWER(account.email) like LOWER(:sSearch_2)";
		}
		if(sSearch_3.length()>0){
			if(h==1)
				hql+= " AND";
			else hql += " WHERE";			
			hql += " LOWER(account.status) like LOWER(:sSearch_3)";
		}
        TypedQuery<Account> query = entityManager().createQuery(hql, Account.class).setFirstResult(iDisplayStart).setMaxResults(iDisplayLength);
        if(sSearch.length()>0)
        	query.setParameter("sSearch", "%"+sSearch+"%");
        if(sSearch_0.length()>0)
        	query.setParameter("sSearch_0", "%"+sSearch_0+"%");
        if(sSearch_1.length()>0)
        	query.setParameter("sSearch_1", "%"+sSearch_1+"%");
        if(sSearch_2.length()>0)
        	query.setParameter("sSearch_2", "%"+sSearch_2+"%");
        if(sSearch_3.length()>0)
        	query.setParameter("sSearch_3", "%"+sSearch_3+"%");
       
        return query.getResultList();
	}
	public Calendar getCalendar() {
		return calendar;
	}
	private void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}
