package com.munna.kmptesting

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.munna.kmptesting.data.UserTable

object DatabaseFactory {
    fun init() {
        Database.connect(createHikariDataSource())
        transaction {
            SchemaUtils.createMissingTablesAndColumns(UserTable)
        }
    }

    private fun createHikariDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        
        // Render and other cloud providers often provide DATABASE_URL
        val dbUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/grocery_bazar"
        config.jdbcUrl = dbUrl
        
        // If DATABASE_URL contains username/password, Hikari might need them separately depending on the URL format
        // For simplicity, we assume standard local setup or Render's DB_USER/DB_PASSWORD env vars
        config.username = System.getenv("DB_USER") ?: System.getProperty("user.name")
        config.password = System.getenv("DB_PASSWORD") ?: ""
        
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
