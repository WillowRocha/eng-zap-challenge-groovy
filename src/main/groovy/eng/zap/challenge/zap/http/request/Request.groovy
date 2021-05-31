package eng.zap.challenge.zap.http.request

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovyjarjarantlr4.v4.runtime.misc.NotNull

import javax.validation.constraints.NotBlank

@EqualsAndHashCode
@ToString(includeNames = true)
class Request {

    @NotNull
    Integer pageNumber = 0

    @NotNull
    Integer pageSize = 20

    @NotBlank
    String orderBy

    @NotNull
    Order order
}
