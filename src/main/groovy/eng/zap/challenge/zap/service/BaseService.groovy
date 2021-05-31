package eng.zap.challenge.zap.service

import eng.zap.challenge.zap.http.request.Order
import groovy.util.logging.Slf4j

@Slf4j
abstract class BaseService {

    protected static <T> List<T> getPage(List<T> list, Integer pageNumber, Integer pageSize) {
        if (!list) return []
        Integer begin = pageNumber * pageSize
        Integer end = (pageNumber + 1) * pageSize

        if (begin < 0 || end < 0 || begin > list.size()) return []

        if (end > list.size()) end = list.size()
        return list.subList(begin, end)
    }

    protected static <T> List<T> orderBy(List<T> list, String orderByAttribute, Order order, String defaultAttribute) {
        if (!list) return []
        if (!orderByAttribute) return list
        List<T> sorted
        try {
            sorted = list.sort { getField(it, orderByAttribute) }
            return order == Order.DESC ? sorted.reverse() : sorted
        } catch (Exception e) {
            log.info("Unable to order by attribute ${orderByAttribute}", e)
            sorted = list.sort { it[defaultAttribute] }
            return order == Order.DESC ? sorted.reverse() : sorted
        }
    }

    private static Object getField(Object it, String attribute) {
        List<String> nextFields = attribute.tokenize('.')
        return recursiveFind(it, nextFields)
    }

    private static Object recursiveFind(Object it, List<String> nextFields) {
        if (nextFields.size() > 1) {
            return recursiveFind(it[nextFields.pop()], nextFields)
        }
        return it[nextFields.pop()]
    }
}
