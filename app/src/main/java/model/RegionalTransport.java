package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Name("Regional transport")
@Entity
@Data
public class RegionalTransport extends PublicTransport implements Serializable {
    @Name("Company owner")
    private String ownerCompany;
    @Name("Regional routes")
    private Map<String, String> regionalRoutes;

    @Override
    public void rideTo(String destAddress) {

    }
}
/*
+ getOwnerCompany(): String
+ setOwnerCompany(String ownerCompany)
+ getRegionalRoutes(): Map<String, String>
+ setRegionalRoutes(Map<String, String> regionalRoutes)
 */