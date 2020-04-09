package model;

import annotations.Entity;
import annotations.Name;

@Name("Personal transport")
@Entity
public class PersonalTransport extends Transport {
    @Name("Transport owner")
    private String personOwner;

    public String getPersonOwner() {
        return personOwner;
    }

    public void setPersonOwner(String personOwner) {
        this.personOwner = personOwner;
    }
}
