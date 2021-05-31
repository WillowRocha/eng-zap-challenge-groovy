package eng.zap.challenge.zap.model.property


import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@EqualsAndHashCode
@ToString(includeNames = true)
class Property {

    Double usableAreas
    ListingType listingType
    LocalDateTime createdAt
    ListingStatus listingStatus
    String id
    Integer parkingSpaces
    LocalDateTime updatedAt
    Boolean owner
    List<String> images
    Address address
    Integer bathrooms
    Integer bedrooms
    PricingInfos pricingInfos
}
