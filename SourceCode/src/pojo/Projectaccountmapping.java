package pojo;
// Generated Mar 10, 2012 10:58:54 AM by Hibernate Tools 3.4.0.CR1



/**
 * Projectaccountmapping generated by hbm2java
 */
public class Projectaccountmapping  implements java.io.Serializable {


     private Integer projectAccountMappingId;
     private Account account;
     private Project project;
     private Role role;

    public Projectaccountmapping() {
    }

    public Projectaccountmapping(Account account, Project project, Role role) {
       this.account = account;
       this.project = project;
       this.role = role;
    }
   
    public Integer getProjectAccountMappingId() {
        return this.projectAccountMappingId;
    }
    
    public void setProjectAccountMappingId(Integer projectAccountMappingId) {
        this.projectAccountMappingId = projectAccountMappingId;
    }
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }




}


