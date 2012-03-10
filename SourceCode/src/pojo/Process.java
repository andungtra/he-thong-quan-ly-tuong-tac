package pojo;
// Generated Mar 10, 2012 10:58:54 AM by Hibernate Tools 3.4.0.CR1


import java.util.HashSet;
import java.util.Set;

/**
 * Process generated by hbm2java
 */
public class Process  implements java.io.Serializable {


     private Integer processId;
     private String name;
     private Set projects = new HashSet(0);
     private Set dependencytypes = new HashSet(0);
     private Set workitemtypes = new HashSet(0);

    public Process() {
    }

	
    public Process(String name) {
        this.name = name;
    }
    public Process(String name, Set projects, Set dependencytypes, Set workitemtypes) {
       this.name = name;
       this.projects = projects;
       this.dependencytypes = dependencytypes;
       this.workitemtypes = workitemtypes;
    }
   
    public Integer getProcessId() {
        return this.processId;
    }
    
    public void setProcessId(Integer processId) {
        this.processId = processId;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Set getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set projects) {
        this.projects = projects;
    }
    public Set getDependencytypes() {
        return this.dependencytypes;
    }
    
    public void setDependencytypes(Set dependencytypes) {
        this.dependencytypes = dependencytypes;
    }
    public Set getWorkitemtypes() {
        return this.workitemtypes;
    }
    
    public void setWorkitemtypes(Set workitemtypes) {
        this.workitemtypes = workitemtypes;
    }




}


