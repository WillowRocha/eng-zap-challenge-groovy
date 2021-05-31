package eng.zap.challenge.zap.model.property

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includeNames = true)
class Address {

    String city
    String neighborhood
    GeoLocation geoLocation

}
