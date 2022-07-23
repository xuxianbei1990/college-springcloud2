package ddd.trackingms.application.internal;

import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.trackingms.domain.model.aggregates.TrackingActivity;
import ddd.trackingms.domain.model.aggregates.TrackingNumber;
import ddd.trackingms.domain.model.commands.AddTrackingEventCommand;
import ddd.trackingms.domain.model.commands.AssignTrackingNumberCommand;
import ddd.trackingms.infrastructure.repositories.jpa.TrackingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:30
 * Version:V1.0
 */
@Service
public class AssignTrackingIdCommandService {

    private TrackingRepository trackingRepository;

    @Transactional // Inititate the Transaction
    public TrackingNumber assignTrackingNumberToCargo(AssignTrackingNumberCommand assignTrackingNumberCommand){
        String random = UUID.randomUUID().toString().toUpperCase();
        String trackingNumber = random.substring(0, random.indexOf("-"));
        assignTrackingNumberCommand.setTrackingNumber(trackingNumber);
        TrackingActivity activity = new TrackingActivity(assignTrackingNumberCommand);

        trackingRepository.save(activity); //Store the Tracking Identifier
        return new TrackingNumber(trackingNumber);
    }


    @Transactional
    public void addTrackingEvent(AddTrackingEventCommand addTrackingEventCommand){
        TrackingActivity trackingActivity = trackingRepository.findByBookingNumber(
                new BookingId(addTrackingEventCommand.getBookingId()));

        trackingActivity.addTrackingEvent(addTrackingEventCommand);
        trackingRepository.save(trackingActivity);
    }
}
