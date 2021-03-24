package ddd.handlingms.cargotracker.applicaiton.internal.outboundservices;

import ddd.shareddomain.event.CargoHandledEvent;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 20:21
 * Version:V1.0
 */
public class CargoEventPublisherService {
    @TransactionalEventListener
    public void handleCargoHandledEvent(CargoHandledEvent cargoHandledEvent) {
//        cargoEventSource.cargoHandling().send(MessageBuilder.withPayload(cargoHandledEvent).build());
    }
}
