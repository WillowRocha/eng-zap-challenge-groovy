package eng.zap.challenge.zap.controller.property

import eng.zap.challenge.zap.http.request.RequestFilter
import eng.zap.challenge.zap.http.response.Response
import eng.zap.challenge.zap.model.property.Property
import eng.zap.challenge.zap.service.property.PropertyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
@RequestMapping('/properties')
class PropertyController {

    @Autowired
    PropertyService service

    @GetMapping('/all')
    Response<Property> getAllProperties(@Valid RequestFilter req) {
        return service.getAllProperties(req)
    }

    @GetMapping('/zap')
    Response<Property> getZapProperties(@Valid RequestFilter req) {
        return service.getZapProperties(req)
    }

    @GetMapping('/vivareal')
    Response<Property> getVivaRealProperties(@Valid RequestFilter req) {
        return service.getVivaRealProperties(req)
    }
}
