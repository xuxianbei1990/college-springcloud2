package ddd.handlingms.cargotracker.interfaces.rest;

import ddd.handlingms.cargotracker.applicaiton.internal.commandservices.HandlingActivityRegistrationCommandService;
import ddd.handlingms.cargotracker.interfaces.rest.dto.HandlingActivityRegistrationResource;
import ddd.handlingms.cargotracker.interfaces.rest.transform.HandlingActivityRegistrationCommandDTOAssembler;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 11:21
 * Version:V1.0
 */
@RestController
@RequestMapping("/cargohandling")
public class CargoHandlingController {

    private HandlingActivityRegistrationCommandService handlingActivityRegistrationCommandService;

    @PostMapping
    @ResponseBody
    public String registerHandlingActivity(@RequestBody HandlingActivityRegistrationResource handlingActivityRegistrationResource){
        System.out.println("***"+handlingActivityRegistrationResource.getBookingId());
        System.out.println("***"+handlingActivityRegistrationResource.getHandlingType());

        handlingActivityRegistrationCommandService.registerHandlingActivityService(HandlingActivityRegistrationCommandDTOAssembler.toCommandFromDTO(handlingActivityRegistrationResource));
        return "Handling Activity Registered";
    }
}
