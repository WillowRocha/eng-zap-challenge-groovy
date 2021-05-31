package eng.zap.challenge.zap.service

import eng.zap.challenge.zap.model.property.Property
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Slf4j
@Service
class DataRetrieverService {

    RestTemplate restTemplate

    @Value('${service.fetchUrl}')
    String fetchUrl

    @Autowired
    DataRetrieverService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }

    List<Property> getProperties() {
        return restTemplate.exchange(
                fetchUrl,
                HttpMethod.GET,
                new HttpEntity<?>(new HttpHeaders()),
                new ParameterizedTypeReference<List<Property>>() {}
        ).body
    }

}
