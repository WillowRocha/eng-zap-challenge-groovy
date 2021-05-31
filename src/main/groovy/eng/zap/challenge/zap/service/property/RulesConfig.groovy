package eng.zap.challenge.zap.service.property

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RulesConfig {

    @Value('${constants.zapBoundBoxMinLon}')
    final static Double ZAP_BOUND_BOX_MIN_LON = -46.693419
    @Value('${constants.zapBoundBoxMinLat}')
    final static Double ZAP_BOUND_BOX_MIN_LAT = -23.568704
    @Value('${constants.zapBoundBoxMaxLon}')
    final static Double ZAP_BOUND_BOX_MAX_LON = -46.641146
    @Value('${constants.zapBoundBoxMaxLat}')
    final static Double ZAP_BOUND_BOX_MAX_LAT = -23.546686

    @Value('${constants.zapMinSalePrice}')
    final static Double ZAP_MIN_SALE_PRICE = 600000
    @Value('${constants.zapMinRentPrice}')
    final static Double ZAP_MIN_RENT_PRICE = 3500
    @Value('${constants.zapMinSquareMeterPrice}')
    final static Double ZAP_MIN_SQUARE_METER_PRICE = 3500

    @Value('${constants.vivaMaxSalePrice}')
    final static Double VIVA_MAX_SALE_PRICE = 700000
    @Value('${constants.vivaMaxRentPrice}')
    final static Double VIVA_MAX_RENT_PRICE = 4000

}
