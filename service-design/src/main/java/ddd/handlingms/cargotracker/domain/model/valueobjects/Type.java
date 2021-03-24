package ddd.handlingms.cargotracker.domain.model.valueobjects;

import lombok.Getter;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:52
 * Version:V1.0
 */
@Getter
public enum Type {

    // Loaded onto voyage from port location.
    LOAD(true),
    // Unloaded from voyage to port location
    UNLOAD(true),
    // Received by carrier
    RECEIVE(false),
    // CargoBookingId claimed by recepient
    CLAIM(false),
    // CargoBookingId went through customs
    CUSTOMS(false);
    private boolean voyageRequired;

    Type(boolean voyageRequired) {
        this.voyageRequired = voyageRequired;
    }

    public boolean prohibitsVoyage() {
        return !isVoyageRequired();
    }
}
