package com.munna.kmptesting.data

import com.munna.kmptesting.DatabaseFactory.dbQuery
import com.munna.kmptesting.RegisterRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository {
    suspend fun registerUser(request: RegisterRequest): Boolean = dbQuery {
        val insertStatement = UserTable.insert {
            it[UserTable.fullName] = request.fullName
            it[UserTable.username] = request.username
            it[UserTable.userType] = request.userType
            it[UserTable.gender] = request.gender
            it[UserTable.address] = request.address
            it[UserTable.password] = request.password
        }
        insertStatement.insertedCount > 0
    }

    suspend fun findUserByUsername(username: String): RegisterRequest? = dbQuery {
        UserTable.selectAll()
            .where { UserTable.username eq username }
            .map { row ->
                RegisterRequest(
                    fullName = row[UserTable.fullName],
                    username = row[UserTable.username],
                    userType = row[UserTable.userType],
                    gender = row[UserTable.gender] ?: "",
                    address = row[UserTable.address] ?: "",
                    password = row[UserTable.password]
                )
            }.singleOrNull()
    }

    suspend fun userExists(username: String): Boolean = dbQuery {
        UserTable.selectAll().where { UserTable.username eq username }
            .any()
    }
}
