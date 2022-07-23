package ddd.bookingms.cargotracker.application.internal.outboundservices;

import ddd.shareddomain.event.CargoBookedEvent;
import ddd.shareddomain.event.CargoRoutedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 10:59
 * Version:V1.0
 */
@Service
public class CargoEventPublisherService {

    @TransactionalEventListener
    public void handleCargoBookedEvent(CargoBookedEvent cargoBookedEvent){
        try {
            System.out.println("****"+cargoBookedEvent);
            System.out.println("****"+cargoBookedEvent.getCargoBookedEventData());
//            cargoEventSource.cargoBooking().send(MessageBuilder.withPayload(cargoBookedEvent).build());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @TransactionalEventListener
    public void handleCargoRoutedEvent(CargoRoutedEvent cargoRoutedEvent){
//        cargoEventSource.cargoRouting().send(MessageBuilder.withPayload(cargoRoutedEvent).build());
    }
}
