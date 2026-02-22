package co.purplegraph.commons.params

import java.util.*

open class PurpleContext(
    var locale: Locale,
    var tenant: String,
    var startTime: String,
    var username: String,
    var permissions: List<String> = ArrayList(),
    var traceId: String = UUID.randomUUID().toString(),
) {

}