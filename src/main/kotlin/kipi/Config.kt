package kipi

import java.util.*

class Config {
    private val properties = Config::class.java.classLoader.getResourceAsStream("config.properties").use {
        Properties().apply { load(it) }
    }

    private val envs = System.getenv()

    private fun get(name: String): String =
        properties.getProperty(name) ?: throw RuntimeException("This property not exist")

    private fun getEnv(name: String): String = envs[name] ?: throw RuntimeException("Env $name not exist")

    val dbHost = getEnv("DB_HOST")
    val dbPort = getEnv("DB_PORT").toInt()
    val dbName = getEnv("DB_NAME")
    val dbUser = getEnv("DB_USER")
    val dbPassword = getEnv("DB_PASSWORD")
    val mailUser = getEnv("MAIL_USER")
    val mailToken = getEnv("MAIL_TOKEN")
    val sessionAccessLiveTimeMin = get("session.access.liveTimeMin").toLong()
    val sessionRefreshLiveTimeMin = get("session.refresh.liveTimeMin").toLong()
}