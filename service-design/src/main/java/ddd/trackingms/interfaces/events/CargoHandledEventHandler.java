package ddd.trackingms.interfaces.events;

import ddd.shareddomain.event.CargoHandledEvent;
import ddd.trackingms.application.internal.AssignTrackingIdCommandService;
import ddd.trackingms.interfaces.events.transform.TrackingActivityCommandEventAssembler;
import org.springframework.stereotype.Service;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:37
 * Version:V1.0
 */
@Service
public class CargoHandledEventHandler {

    private AssignTrackingIdCommandService assignTrackingIdCommandService;


    /**
     * 这里是通过消息监听实现的
     * @param cargoHandledEvent
     */
//    @StreamListener(target = HandlingBinding.HANDLING)
    public void receiveEvent(CargoHandledEvent cargoHandledEvent) {
        //Process the Event
        System.out.println("XXXXXXXXXX"+cargoHandledEvent.getCargoHandledEventData());
        assignTrackingIdCommandService.addTrackingEvent(
                TrackingActivityCommandEventAssembler.toCommandFromEvent(cargoHandledEvent));

    }
}
