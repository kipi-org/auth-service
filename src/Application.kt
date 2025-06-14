import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import db.DataSourceConfigurator
import db.DbMigration
import controllers.response.ErrorResponse
import exceptions.AuthException
import exceptions.ValidationException
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), host = "0.0.0.0", module = Application::init)
        .start(wait = true)
}

fun Application.init() {
    val mapper = jsonMapper {
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        serializationInclusion(JsonInclude.Include.NON_NULL)
        enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
        addModule(JavaTimeModule())
        addModule(kotlinModule { configure(KotlinFeature.SingletonSupport, true) })
    }

    install(ContentNegotiation) {
        register(Json, JacksonConverter(mapper))
    }
    install(CallLogging) { loggerConfiguration() }

    install(StatusPages) {
        exception<AuthException> { call, cause ->
            call.respond(Forbidden, ErrorResponse(cause.message!!))
        }

        exception<ValidationException> { call, cause ->
            call.respond(Forbidden, cause.message!!)
        }

        exception<ValidationException> { call, cause ->
            call.respond(UnprocessableEntity, cause.errors)
        }
    }

    val deps = Dependencies()

    initMigration(deps.config)
    routes(deps)
}

private fun initMigration(config: Config) {
    DataSourceConfigurator(config).also {
        val dataSource = it.createDataSource()
        Database.connect(dataSource)
        DbMigration(dataSource).migrate()
    }
}