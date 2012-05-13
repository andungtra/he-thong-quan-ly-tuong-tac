// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.WorkItemContainer;

privileged aspect Iteration_Roo_Finder {
    
    public static TypedQuery<Iteration> Iteration.findIterationsByParentContainer(WorkItemContainer parentContainer) {
        if (parentContainer == null) throw new IllegalArgumentException("The parentContainer argument is required");
        EntityManager em = Iteration.entityManager();
        TypedQuery<Iteration> q = em.createQuery("SELECT o FROM Iteration AS o WHERE o.parentContainer = :parentContainer", Iteration.class);
        q.setParameter("parentContainer", parentContainer);
        return q;
    }
    
}