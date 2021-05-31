package eng.zap.challenge.zap.controller.retriever

import eng.zap.challenge.zap.model.property.Property
import eng.zap.challenge.zap.service.DataRetrieverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/data')
class DataRetrieverController {

    @Autowired
    DataRetrieverService service

    @GetMapping('/raw')
    List<Property> getRawProperties() {
        return service.getProperties()
    }
}
