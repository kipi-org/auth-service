package kipi

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kipi.dto.Credentials
import kipi.dto.IdCredentials

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        get("/health") {
            call.respond(OK)
        }

        post<Credentials>("/registration") {
            val sessionResponse = registrationController.handle(it)

            call.respond(OK, sessionResponse)
        }

        post<Credentials>("/login") {
            val sessionResponse = loginController.handle(it)

            call.respond(OK, sessionResponse)
        }

        post<IdCredentials>("/loginById") {
            val sessionResponse = loginByIdController.handle(it)

            call.respond(OK, sessionResponse)
        }

        post("/logout") {
            logoutController.handle(call.token)

            call.respond(OK)
        }

        get("/verify") {
            val sessionResponse = verifyController.handle(call.token)

            call.respond(OK, sessionResponse)
        }

        post("/revoke") {
            val sessionResponse = revokeController.handle(call.token)

            call.respond(OK, sessionResponse)
        }
    }
}

private val ApplicationCall.token: String get() = this.parameters.getOrFail("token")
