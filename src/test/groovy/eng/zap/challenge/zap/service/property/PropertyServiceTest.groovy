package eng.zap.challenge.zap.service.property

import eng.zap.challenge.zap.AppSpec
import eng.zap.challenge.zap.http.request.RequestFilter
import eng.zap.challenge.zap.model.property.Address
import eng.zap.challenge.zap.model.property.BusinessType
import eng.zap.challenge.zap.model.property.GeoLocation
import eng.zap.challenge.zap.model.property.Location
import eng.zap.challenge.zap.model.property.PricingInfos
import eng.zap.challenge.zap.model.property.Property
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import java.time.LocalDateTime

import static eng.zap.challenge.zap.model.property.BusinessType.RENTAL
import static eng.zap.challenge.zap.model.property.BusinessType.SALE
import static eng.zap.challenge.zap.model.property.ListingStatus.ACTIVE
import static eng.zap.challenge.zap.model.property.ListingType.USED

@SpringBootTest
class PropertyServiceTest extends AppSpec {

    @Autowired
    PropertyService service

    def "ApplyUserFilters"() {
        when:
        List<Property> result = PropertyService.applyUserFilters(filter, list)
        then:
        result == expected
        where:
        list      | filter                                       | expected
        MOCK_DATA | new RequestFilter(businessTypes: [SALE])     | [MOCK_DATA[0]]
        MOCK_DATA | new RequestFilter(businessTypes: [RENTAL])   | [MOCK_DATA[1], MOCK_DATA[2]]
        MOCK_DATA | new RequestFilter(maximumUsableAreas: 50)    | [MOCK_DATA[1]]
        MOCK_DATA | new RequestFilter(minimumUsableAreas: 50)    | [MOCK_DATA[0], MOCK_DATA[2]]
        MOCK_DATA | new RequestFilter(minimumParkingSpaces: 2)   | [MOCK_DATA[2]]
        []        | new RequestFilter(listingTypes: [USED])      | []
        null      | new RequestFilter(listingStatuses: [ACTIVE]) | []
    }

    def "is inside Zap bounding box"() {
        when:
        Boolean result = service.isInZapBoundingBox(property)
        then:
        result == expected
        where:
        property                                      | expected
        getGeoLocatedProperty(-23.609441, -46.614014) | false
        getGeoLocatedProperty(-23.553518, -46.687609) | true
        getGeoLocatedProperty(-23.609504, -46.659002) | false
    }

    def "is eligible for Zap"() {
        when:
        Boolean result = service.isEligibleToZap(property)
        then:
        result == expected
        where:
        property                                                              | expected
        getNewProperty(-23.609441, -46.614014, SALE, 605000, null, null, 300) | false // fails with square meter too low
        getNewProperty(-23.553518, -46.687609, SALE, 550000, null, null, 100) | true // pass with price inside BBox
        getNewProperty(-23.609504, -46.659002, SALE, 589000, null, null, 50)  | false // fails in price outside Bbox
        getNewProperty(-23.609441, -46.614014, RENTAL, null, 3700, 800, 50)   | true
        getNewProperty(-23.553518, -46.687609, RENTAL, null, 2500, 900, 40)   | false // fails in price
        getNewProperty(-23.609504, -46.659002, RENTAL, null, 4000, 2000, 60)  | true
    }

    def "is eligible for Viva Real"() {
        when:
        Boolean result = service.isEligibleToVivaReal(property)
        then:
        result == expected
        where:
        property                                                              | expected
        getNewProperty(-23.609441, -46.614014, SALE, 605000, null, null, 300) | true
        getNewProperty(-23.553518, -46.687609, SALE, 740000, null, null, 100) | false // fails with price too high
        getNewProperty(-23.609504, -46.659002, SALE, 589000, null, null, 50)  | true
        getNewProperty(-23.609441, -46.614014, RENTAL, null, 1800, 650, 50)   | false // fails with condo fee too high
        getNewProperty(-23.553518, -46.687609, RENTAL, null, 5000, 900, 40)   | true // pass with price inside BBox
        getNewProperty(-23.609504, -46.659002, RENTAL, null, 4200, 997, 60)   | false // fails with price too high
    }

    protected static getNewProperty(Double lat, Double lon, BusinessType businessType, Double price, Double totalRentPrice, Double condoFee, Double usableAreas) {
        return new Property(
                usableAreas: usableAreas,
                pricingInfos: new PricingInfos(businessType: businessType, price: price, rentalTotalPrice: totalRentPrice, monthlyCondoFee: condoFee),
                address: new Address(geoLocation: new GeoLocation(location: new Location(lat: lat, lon: lon)))
        )
    }

    protected static getGeoLocatedProperty(Double lat, Double lon) {
        return new Property(address: new Address(geoLocation: new GeoLocation(location: new Location(lat: lat, lon: lon))))
    }

    final static List<Property> MOCK_DATA = [
            new Property(
                    usableAreas: 75.0, listingType: USED,
                    createdAt: LocalDateTime.of(2017, 8, 4, 1, 49, 39),
                    listingStatus: ACTIVE, id: '1106a85a38fa', parkingSpaces: 1,
                    updatedAt: LocalDateTime.of(2017, 8, 4, 1, 49, 39),
                    owner: false, images: [], address: null, bathrooms: 2, bedrooms: 2,
                    pricingInfos: new PricingInfos(yearlyIptu: 100.0, price: 276000, rentalTotalPrice: null, businessType: SALE, monthlyCondoFee: 0.0)),
            new Property(
                    usableAreas: 45.0, listingType: USED,
                    createdAt: LocalDateTime.of(2018, 3, 26, 14, 26, 20),
                    listingStatus: ACTIVE, id: '548289d0d1df', parkingSpaces: 1,
                    updatedAt: LocalDateTime.of(2018, 3, 26, 14, 26, 20),
                    owner: false, images: [], address: null, bathrooms: 1, bedrooms: 2,
                    pricingInfos: new PricingInfos(yearlyIptu: 0.0, price: 1200.0, rentalTotalPrice: 1460.0, businessType: RENTAL, monthlyCondoFee: 260.0)
            ),
            new Property(
                    usableAreas: 54.0, listingType: USED,
                    createdAt: LocalDateTime.of(2018, 6, 14, 04, 50, 58),
                    listingStatus: ACTIVE, id: 'c4f6894e7b95', parkingSpaces: 2,
                    updatedAt: LocalDateTime.of(2018, 6, 14, 04, 50, 58),
                    owner: false, images: [], address: null, bathrooms: 2, bedrooms: 0,
                    pricingInfos: new PricingInfos(yearlyIptu: 538.0, price: 2190.0, rentalTotalPrice: 3327.0, businessType: RENTAL, monthlyCondoFee: 1137.0)
            )
    ]
}
