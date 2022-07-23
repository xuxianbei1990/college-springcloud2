package ddd.handlingms.cargotracker.interfaces.rest.transform;

import ddd.handlingms.cargotracker.domain.model.commands.HandlingActivityRegistrationCommand;
import ddd.handlingms.cargotracker.interfaces.rest.dto.HandlingActivityRegistrationResource;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 20:18
 * Version:V1.0
 */
public class HandlingActivityRegistrationCommandDTOAssembler {

    /**
     * Static method within the Assembler class
     * @param handlingActivityRegistrationResource
     * @return BookCargoCommand Model
     */
    public static HandlingActivityRegistrationCommand toCommandFromDTO(HandlingActivityRegistrationResource handlingActivityRegistrationResource){

        System.out.println("Booking Id "+handlingActivityRegistrationResource.getBookingId());
        System.out.println("Voyage Number"+handlingActivityRegistrationResource.getVoyageNumber());
        System.out.println("Unlocode"+handlingActivityRegistrationResource.getUnLocode());
        System.out.println("HandlingType"+handlingActivityRegistrationResource.getHandlingType());
        System.out.println("Completion Time"+handlingActivityRegistrationResource.getCompletionTime());
        return new HandlingActivityRegistrationCommand(
                handlingActivityRegistrationResource.getBookingId(),
                handlingActivityRegistrationResource.getVoyageNumber(),
                handlingActivityRegistrationResource.getUnLocode(),
                handlingActivityRegistrationResource.getHandlingType(),
                java.sql.Date.valueOf(handlingActivityRegistrationResource.getCompletionTime())
        );
    }
}
