package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

@Data
@Name("Bus stop")
@Entity
public class BusStop {
    @Name("Location")
    private String address;
    @Name("Stop title")
    private String title;
    @Name("Index")
    private int indexNumber;


}
/*


 */