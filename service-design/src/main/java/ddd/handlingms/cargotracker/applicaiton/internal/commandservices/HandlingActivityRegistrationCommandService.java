package ddd.handlingms.cargotracker.applicaiton.internal.commandservices;

import ddd.bookingms.cargotracker.domain.model.entities.Location;
import ddd.handlingms.cargotracker.domain.model.aggregates.HandlingActivity;
import ddd.handlingms.cargotracker.domain.model.commands.HandlingActivityRegistrationCommand;
import ddd.handlingms.cargotracker.domain.model.valueobjects.CargoBookingId;
import ddd.handlingms.cargotracker.domain.model.valueobjects.Type;
import ddd.handlingms.cargotracker.domain.model.valueobjects.VoyageNumber;
import ddd.handlingms.cargotracker.infrastructure.repositories.jpa.HandlingActivityRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:44
 * Version:V1.0
 */
@Service
public class HandlingActivityRegistrationCommandService {

    private HandlingActivityRepository handlingActivityRepository;

    @Transactional
    public void registerHandlingActivityService(HandlingActivityRegistrationCommand handlingActivityRegistrationCommand) {
        System.out.println("Handling Voyage Number is" + handlingActivityRegistrationCommand.getVoyageNumber());
        if (!handlingActivityRegistrationCommand.getVoyageNumber().equals("")) {
            HandlingActivity handlingActivity = new HandlingActivity(
                    new CargoBookingId(handlingActivityRegistrationCommand.getBookingId()),
                    handlingActivityRegistrationCommand.getCompletionTime(),
                    Type.valueOf(handlingActivityRegistrationCommand.getHandlingType()),
                    new Location(handlingActivityRegistrationCommand.getUnLocode()),
                    new VoyageNumber(handlingActivityRegistrationCommand.getVoyageNumber()));

            handlingActivityRepository.save(handlingActivity);
        }
    }


}
