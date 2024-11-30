package controllers.request

data class OtpConfirmationRequest(
    val phoneNumber: String,
    val otpCode: String,
)
