package com.munna.kmptesting.data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val fullName = varchar("full_name", 255)
    val username = varchar("username", 50).uniqueIndex()
    val userType = varchar("user_type", 20)
    val gender = varchar("gender", 20).nullable()
    val address = text("address").nullable()
    val password = varchar("password", 255)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}
