package kipi

import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kipi.db.DataSourceConfigurator
import kipi.db.DbMigration
import kipi.dto.ErrorResponse
import kipi.exceptions.AuthException
import kipi.exceptions.SessionException
import kipi.exceptions.ValidationException
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 7005, host = "0.0.0.0", module = Application::init)
        .start(wait = true)
}

fun Application.init() {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<AuthException> { call, cause ->
            call.respond(Forbidden, ErrorResponse(cause.message!!))
        }

        exception<SessionException> { call, cause ->
            call.respond(Unauthorized, ErrorResponse(cause.message!!))
        }

        exception<ValidationException> { call, cause ->
            call.respond(UnprocessableEntity, cause.errors)
        }
    }

    val deps = Dependencies()

    initMigration()
    routes(deps)
}

private fun initMigration() {
    DataSourceConfigurator().also {
        val dataSource = it.createDataSource()
        Database.connect(dataSource)
        DbMigration(dataSource).migrate()
    }
}