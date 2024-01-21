package kipi

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        post("/registration") {
            val sessionResponse = registrationController.handle(call.receive())

            call.respond(OK, sessionResponse)
        }

        post("/login") {
            val sessionResponse = loginController.handle(call.receive())

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
