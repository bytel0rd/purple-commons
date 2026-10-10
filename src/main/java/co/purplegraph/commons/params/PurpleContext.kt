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

fun validatePermission(ctx: PurpleContext, vararg required:  String): Boolean {
    println(ctx.permissions);

    if (ctx.permissions.contains("master")) {
        return true;
    }

    return required.all { r ->  ctx.permissions.contains(r) }

}