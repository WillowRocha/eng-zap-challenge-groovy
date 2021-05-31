package eng.zap.challenge.zap.service.property

import eng.zap.challenge.zap.http.request.RequestFilter
import eng.zap.challenge.zap.http.response.Response
import eng.zap.challenge.zap.model.property.BusinessType
import eng.zap.challenge.zap.model.property.Property
import eng.zap.challenge.zap.service.BaseService
import eng.zap.challenge.zap.service.DataRetrieverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service
class PropertyService extends BaseService {

    DataRetrieverService retrieverService
    List<Property> loadedProperties = []

    @Autowired
    PropertyService(DataRetrieverService retrieverService) {
        this.retrieverService = retrieverService
    }

    @PostConstruct
    void init() {
        this.loadedProperties = retrieverService.getAllAvailableProperties()
    }

    Response<Property> getAllProperties(RequestFilter filter) {
        return getAvailableProperties(filter, { Property it -> it && hasValidGeoLocation(it) })
    }

    Response<Property> getZapProperties(RequestFilter filter) {
        return getAvailableProperties(filter, { Property it -> isEligibleToZap(it) })
    }

    Response<Property> getVivaRealProperties(RequestFilter filter) {
        return getAvailableProperties(filter, { Property it -> isEligibleToVivaReal(it) })
    }

    protected Response<Property> getAvailableProperties(RequestFilter filter, Closure<Boolean> isEligible) {
        List<Property> properties = this.loadedProperties
        Integer totalCount = 0
        if (properties) {
            properties = properties.findAll { isEligible(it) }
            properties = applyUserFilters(filter, properties)
            totalCount = properties.size()
            properties = orderBy(properties, filter.orderBy, filter.order, "id")
            properties = getPage(properties, filter.pageNumber, filter.pageSize)
        }
        return new Response<Property>(listings: properties, totalCount: totalCount, pageNumber: filter.pageNumber, pageSize: filter.pageSize)
    }

    protected static List<Property> applyUserFilters(RequestFilter filter, List<Property> properties) {
        if (!properties) return []
        if (filter.businessTypes) {
            properties = properties.findAll { filter.businessTypes.contains(it.pricingInfos.businessType) }
        }
        if (filter.listingStatuses) {
            properties = properties.findAll { filter.listingStatuses.contains(it.listingStatus) }
        }
        if (filter.listingTypes) {
            properties = properties.findAll { filter.listingTypes.contains(it.listingType) }
        }
        if (filter.minimumParkingSpaces) {
            properties = properties.findAll { it.parkingSpaces >= filter.minimumParkingSpaces }
        }
        if (filter.maximumParkingSpaces) {
            properties = properties.findAll { it.parkingSpaces <= filter.maximumParkingSpaces }
        }
        if (filter.minimumUsableAreas) {
            properties = properties.findAll { it.usableAreas >= filter.minimumUsableAreas }
        }
        if (filter.maximumUsableAreas) {
            properties = properties.findAll { it.usableAreas <= filter.maximumUsableAreas }
        }
        if (filter.minimumPrice) {
            properties = properties.findAll { it.pricingInfos.price >= filter.minimumPrice }
        }
        if (filter.maximumPrice) {
            properties = properties.findAll { it.pricingInfos.price <= filter.maximumPrice }
        }
        if (filter.minimumCondoFee) {
            properties = properties.findAll { it.pricingInfos.monthlyCondoFee >= filter.minimumCondoFee }
        }
        if (filter.maximumCondoFee) {
            properties = properties.findAll { it.pricingInfos.monthlyCondoFee <= filter.maximumCondoFee }
        }
        return properties
    }

    protected static Boolean isEligibleToZap(Property it) {
        if (!it) return false
        Double minSalePrice = RulesConfig.ZAP_MIN_SALE_PRICE
        if (isInZapBoundingBox(it)) {
            minSalePrice = minSalePrice * 0.9
        }
        Boolean saleEligible = isForSale(it) && (
                hasValidUsableAreas(it) && getSaleSquareMeterPrice(it) > RulesConfig.ZAP_MIN_SQUARE_METER_PRICE
                        && getSalePrice(it) >= minSalePrice
        )
        Boolean rentalEligible = isForRent(it) && (
                getRentPrice(it) >= RulesConfig.ZAP_MIN_RENT_PRICE
        )
        return hasValidGeoLocation(it) && saleEligible || rentalEligible
    }

    protected static Boolean isEligibleToVivaReal(Property it) {
        if (!it) return false
        Boolean saleEligible = isForSale(it) && (
                getSalePrice(it) <= RulesConfig.VIVA_MAX_SALE_PRICE
        )
        Double maxRentPrice = RulesConfig.VIVA_MAX_RENT_PRICE
        if (isInZapBoundingBox(it)) {
            maxRentPrice = maxRentPrice * 1.5
        }
        Boolean rentalEligible = isForRent(it) && (
                hasValidCondoFee(it) && getCondoFee(it) < getRentPrice(it) * 0.3
                        && getRentPrice(it) <= maxRentPrice
        )
        return hasValidGeoLocation(it) && saleEligible || rentalEligible
    }

    protected static Boolean isInZapBoundingBox(Property it) {
        Double lat = it.address.geoLocation.location.lat
        Double lon = it.address.geoLocation.location.lon
        return lat >= RulesConfig.ZAP_BOUND_BOX_MIN_LAT &&
                lat <= RulesConfig.ZAP_BOUND_BOX_MAX_LAT &&
                lon >= RulesConfig.ZAP_BOUND_BOX_MIN_LON &&
                lon <= RulesConfig.ZAP_BOUND_BOX_MAX_LON
    }

    private static Boolean hasValidGeoLocation(Property it) {
        return it.address.geoLocation.location.lat != 0 || it.address.geoLocation.location.lon != 0
    }

    private static Boolean isForSale(Property it) {
        return it.pricingInfos.businessType == BusinessType.SALE
    }

    private static Boolean isForRent(Property it) {
        return it.pricingInfos.businessType == BusinessType.RENTAL
    }

    private static Boolean hasValidUsableAreas(Property it) {
        return it.usableAreas > 0
    }

    private static Boolean hasValidCondoFee(Property it) {
        return it.pricingInfos.monthlyCondoFee != null
        // Zero e null são válidos? Assumi que apenas o null é inválido
    }

    private static Double getSalePrice(Property it) {
        return it.pricingInfos.price
    }

    private static Double getRentPrice(Property it) {
        return it.pricingInfos.rentalTotalPrice
    }

    private static Double getCondoFee(Property it) {
        return it.pricingInfos.monthlyCondoFee
    }

    private static Double getSaleSquareMeterPrice(Property it) {
        if (!it.usableAreas) return null
        return it.pricingInfos.price / it.usableAreas
    }
}
