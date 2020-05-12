package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

import java.io.Serializable;

@Name("Personal transport")
@Entity
@Data
public class PersonalTransport extends Transport implements Serializable {
    @Name("Transport owner")
    private String personOwner;

}
