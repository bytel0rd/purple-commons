package co.purplegraph.commons.services;

import co.purplegraph.commons.params.PurpleContext
import co.purplegraph.commons.utils.DATE_TIME_PATTERN
import co.purplegraph.commons.utils.TENANT_HEADER
import co.purplegraph.commons.utils.TRACE_ID_KEY
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.ObjectProvider
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
@RequiredArgsConstructor
class ContextService(
    private val requestProvider: ObjectProvider<HttpServletRequest>
) {


    fun getContextFromRequest(): PurpleContext {
        val request = requestProvider.ifAvailable

        val authentication = SecurityContextHolder
            .getContext()
            .authentication

        var username = "GUEST"
        var tenant = "EMPTY"

        var permissions: List<String> = emptyList()

        if (authentication != null && authentication.isAuthenticated) {
            when (authentication) {
                is AnonymousAuthenticationToken -> {
                    username = authentication.name
                    permissions = emptyList()
                }
                is JwtAuthenticationToken -> {
                    val split = authentication.name.split("-_");
                    tenant = split.first();
                    username = split.last()
                    permissions = authentication.authorities.map { it.authority.orEmpty().replace("SCOPE_", "") }.toList()
                }
                is UsernamePasswordAuthenticationToken -> {
                    username = authentication.name
                    permissions = authentication.authorities.map { it.authority.orEmpty().replace("SCOPE_", "") }.toList()
                }
                is BearerTokenAuthenticationToken -> {
                    username = authentication.name
                    permissions = authentication.authorities.map { it.authority.orEmpty().replace("SCOPE_", "") }.toList()
                }
                is BearerTokenAuthentication -> {
                    username = authentication.name
                    permissions = authentication.authorities.map { it.authority.orEmpty().replace("SCOPE_", "") }.toList()
                }
            }
        }

        if (tenant == "EMPTY" || tenant == "PURPLE") {
            tenant = request?.getHeader(TENANT_HEADER) ?: tenant
        }

        return PurpleContext(
            locale = request?.locale ?: Locale.ENGLISH,
            tenant = tenant,
            startTime = ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)),
            username = username,
            permissions = permissions,
            traceId = request?.getHeader(TRACE_ID_KEY)
                ?: UUID.randomUUID().toString(),
        )
    }

    fun getPurpleContext(): PurpleContext =
        PurpleContext(
            locale = Locale.ENGLISH,
            tenant = "PURPLE",
            startTime = ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)),
            username = "DEFAULT",
            permissions = listOf("mocked"),
            traceId = UUID.randomUUID().toString(),
        )
}