package eng.zap.challenge.zap.model.property

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString(includeNames = true)
class PricingInfos {

    Double yearlyIptu
    Double price
    Double rentalTotalPrice
    BusinessType businessType
    Double monthlyCondoFee
}
