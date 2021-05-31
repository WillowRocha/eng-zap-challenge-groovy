package eng.zap.challenge.zap.http.response

class Response<Model> {

    Integer pageNumber
    Integer pageSize
    Integer totalCount
    List<Model> listings
}
