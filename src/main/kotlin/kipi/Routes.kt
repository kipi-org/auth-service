package kipi

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kipi.dto.*

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        get("/health") {
            call.respond(OK)
        }

        post<Credentials>("/registration") {
            val sessionResponse = registrationController.handle(it)

            call.respond(OK, sessionResponse)
        }

        route("/email"){
            post<EmailRequest> {
                emailController.handle(it)

                call.respond(OK)
            }

            post<EmailConfirmRequest>("/confirm") {
                emailConfirmController.handle(it)

                call.respond(OK)
            }
        }

        post<EmailConfirmRequest>("/email/confirm") {
            emailConfirmController.handle(it)

            call.respond(OK)
        }

        post<Credentials>("/login") {
            val sessionResponse = loginController.handle(it)

            call.respond(OK, sessionResponse)
        }

        route("/recover") {
            post<RecoverRequest> {
                recoverController.handle(it)

                call.respond(OK)
            }

            post<RecoverConfirmRequest>("/confirm") {
                recoverConfirmController.handle(it)

                call.respond(OK)
            }
        }

        post<IdCredentials>("/loginById") {
            val sessionResponse = loginByIdController.handle(it)

            call.respond(OK, sessionResponse)
        }

        post("/logout") {
            logoutController.handle(call.token)

            call.respond(OK)
        }

        delete("/user/{userId}") {
            deleteUserController.handle(call.userId)

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
private val ApplicationCall.userId: Long get() = this.parameters.getOrFail("userId").toLong()
