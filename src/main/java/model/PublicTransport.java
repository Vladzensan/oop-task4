package model;

import annotations.Entity;
import annotations.Name;
import lombok.Data;

@Data
@Entity
public abstract class PublicTransport extends Transport {
    @Name("Standing places")
    private int standingPlaces;
    @Name("Ticket price")
    private double ticketPrice;
    @Name("Credit card pay")
    private boolean creditCardPay;
    @Name("Subscription card pay")
    private boolean subscriptionCardPay;

    public abstract void rideTo(String destAddress);
}
