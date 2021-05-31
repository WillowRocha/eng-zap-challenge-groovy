package eng.zap.challenge.zap.http.request

import eng.zap.challenge.zap.model.property.BusinessType
import eng.zap.challenge.zap.model.property.ListingStatus
import eng.zap.challenge.zap.model.property.ListingType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(callSuper = true)
@ToString(includeNames = true, includeSuper = true)
class RequestFilter extends Request {

    List<BusinessType> businessTypes = []
    List<ListingStatus> listingStatuses = []
    List<ListingType> listingTypes = []

    Integer minimumParkingSpaces
    Integer maximumParkingSpaces

    Double minimumUsableAreas
    Double maximumUsableAreas

    Double minimumPrice
    Double maximumPrice

    Double minimumCondoFee
    Double maximumCondoFee

}
