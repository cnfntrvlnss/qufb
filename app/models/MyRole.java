package models;

import be.objectify.deadbolt.java.models.Role;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class MyRole implements Role {
    private String id;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Transient
    @Override
    public String getName() {
        return id;
    }
}
