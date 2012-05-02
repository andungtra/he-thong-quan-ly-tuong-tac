// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hcmus.tis.model.Event;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Event_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Event.entityManager;
    
    public static final EntityManager Event.entityManager() {
        EntityManager em = new Event().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Event.countEvents() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Event o", Long.class).getSingleResult();
    }
    
    public static List<Event> Event.findAllEvents() {
        return entityManager().createQuery("SELECT o FROM Event o", Event.class).getResultList();
    }
    
    public static Event Event.findEvent(Long id) {
        if (id == null) return null;
        return entityManager().find(Event.class, id);
    }
    
    public static List<Event> Event.findEventEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Event o", Event.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Event.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Event.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Event attached = Event.findEvent(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Event.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Event.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Event Event.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Event merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
