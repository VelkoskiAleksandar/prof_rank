package com.diplomska.prof_rank.entities;

import mk.ukim.finki.isis.model.entities.Person;
import org.apache.tapestry5.beaneditor.NonVisual;

import javax.persistence.*;

/**
 * Created by Aleksandar on 06-Oct-16.
 */
@Entity
public class ReferenceInstancePerson {
    private Long id;

    private ReferenceInstance referenceInstance;

    private Person person;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonVisual
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "referenceInstance_id")
    public ReferenceInstance getReferenceInstance() {
        return referenceInstance;
    }

    public void setReferenceInstance(ReferenceInstance referenceInstance) {
        this.referenceInstance = referenceInstance;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}