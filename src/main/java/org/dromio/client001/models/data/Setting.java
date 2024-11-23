package org.dromio.client001.models.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Setting {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    private String id;

    private String name;

    private boolean enabled;

    public Setting(String id, String name, boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

    public Setting() {

    }

    public String getId() {
        return id;
    }

    public void setKey(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
