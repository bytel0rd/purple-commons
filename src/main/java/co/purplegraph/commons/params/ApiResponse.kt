package co.purplegraph.commons.params

class ApiResponse<T>(val data: T?, val errorMessage: String?, val code: Int, val message: String?, val errorFields: Any?=null) {

    companion object {

        fun <T> ok(data: T): ApiResponse<T> {
            return ApiResponse(data, null, 0, "Operation successful")
        }

        fun <T> error(errorMessage: String): ApiResponse<T> {
            return ApiResponse(null, errorMessage, 10, null)
        }

        fun <T> errorWithFields(errorMessage: String, errorFields: Any? = null): ApiResponse<T> {
            return ApiResponse(null, errorMessage, 10, null, errorFields)
        }

    }

}