package db

import Config
import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

class DataSourceConfigurator(
    private val config: Config
) {
    fun createDataSource(): DataSource {
        val dataSource = PGSimpleDataSource()
        dataSource.serverNames = arrayOf(config.dbHost)
        dataSource.portNumbers = intArrayOf(config.dbPort)
        dataSource.databaseName = config.dbName
        dataSource.user = config.dbUser
        dataSource.password = config.dbPassword

        return dataSource
    }
}