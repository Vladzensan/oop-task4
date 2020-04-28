package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

@Name("Personal transport")
@Entity
@Data
public class PersonalTransport extends Transport {
    @Name("Transport owner")
    private String personOwner;

}
