import controllers.request.LoginRequest
import controllers.request.OtpConfirmationRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        get("/health") {
            call.respond(OK)
        }

        route("/login") {
            post<LoginRequest> {
                loginController.login(it)

                call.respond(OK)
            }

            post<OtpConfirmationRequest>("/confirm") {
                val sessions = loginController.loginConfirm(it)

                call.respond(OK, sessions)
            }
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
