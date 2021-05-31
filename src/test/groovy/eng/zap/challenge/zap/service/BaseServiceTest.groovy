package eng.zap.challenge.zap.service

import eng.zap.challenge.zap.model.property.PricingInfos
import eng.zap.challenge.zap.model.property.Property
import spock.lang.Specification

import java.time.LocalDateTime

import static eng.zap.challenge.zap.http.request.Order.ASC
import static eng.zap.challenge.zap.http.request.Order.DESC
import static eng.zap.challenge.zap.model.property.BusinessType.RENTAL
import static eng.zap.challenge.zap.model.property.BusinessType.SALE
import static eng.zap.challenge.zap.model.property.ListingStatus.ACTIVE
import static eng.zap.challenge.zap.model.property.ListingType.USED

class BaseServiceTest extends Specification {

    def "GetPage"() {
        given:
        List<Integer> list = (0..49)
        when:
        List<Integer> result = BaseService.getPage(list, page, pageSize)
        then:
        result == expected
        where:
        page | pageSize | expected
        0    | 20       | (0..19)
        1    | 20       | (20..39)
        0    | 100      | (0..49)
        1    | 50       | []
        2    | 5        | (10..14)
        0    | -5       | []
    }

    def "orderBy"() {
        when:
        List<Property> result = BaseService.orderBy(mockData, orderBy, order, 'id')
        then:
        result == expected
        where:
        mockData  | orderBy                         | order | expected
        MOCK_DATA | 'pricingInfos.price'            | ASC   | [MOCK_DATA[1], MOCK_DATA[2], MOCK_DATA[0]]
        MOCK_DATA | 'pricingInfos.price'            | DESC  | [MOCK_DATA[0], MOCK_DATA[2], MOCK_DATA[1]]
        MOCK_DATA | 'pricingInfos.rentalTotalPrice' | ASC   | [MOCK_DATA[0], MOCK_DATA[1], MOCK_DATA[2]]
        MOCK_DATA | 'pricingInfos.yearlyIptu'       | DESC  | [MOCK_DATA[2], MOCK_DATA[0], MOCK_DATA[1]]
        MOCK_DATA | 'usableAreas'                   | ASC   | [MOCK_DATA[1], MOCK_DATA[2], MOCK_DATA[0]]
        MOCK_DATA | null                            | null  | MOCK_DATA
        null      | null                            | null  | []

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
