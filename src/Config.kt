class Config {
    private val envs = System.getenv()

    private fun getEnv(name: String): String = envs[name] ?: throw RuntimeException("Env $name not exist")

    val dbHost = getEnv("DB_HOST")
    val dbPort = getEnv("DB_PORT").toInt()
    val dbName = getEnv("DB_NAME")
    val dbUser = getEnv("DB_USER")
    val dbPassword = getEnv("DB_PASSWORD")
    val notificationsServiceUrl = getEnv("NOTIFICATIONS_SERVICE_URl")
    val sessionAccessLiveTimeMin = getEnv("SESSION_LIVE_TIME_MIN").toLong()
}