package co.purplegraph.commons.params

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*

class LocaleContext(
    locale: Locale, tenant: String, startTime: String, username: String, permissions: List<String> = ArrayList(),
    traceId: String = UUID.randomUUID().toString(),
) :
    PurpleContext(
        locale, tenant, startTime,
        username, permissions, traceId
    ) {

    companion object {
        private val log = KotlinLogging.logger {}

        fun from(purple: PurpleContext): LocaleContext {
            return LocaleContext(
                locale = purple.locale,
                tenant = purple.tenant,
                startTime = purple.startTime,
                username = purple.username,
            )
        }
    }

    private val bundle: ResourceBundle = try {
        ResourceBundle.getBundle("Messages", locale)
    } catch (e: MissingResourceException) {
        log.error(e) { "Resource bundle 'Messages' not found for locale $locale" }
        ResourceBundle.getBundle("Messages", Locale.ENGLISH) // fallback
    }

    fun getMessage(code: String): String {
        try {
            return bundle.getString(code)
        } catch (e: Exception) {
            log.error(e) { "Unable to fetch message: $locale - $code " }
        }
        return code
    }

    fun formatMessage(code: String, map: Map<String, String>): String {
        var message = getMessage(code)
        for ((key, value) in map) {
            message = message.replace("[{${key.uppercase()}}]", value)
        }
        return message
    }


}