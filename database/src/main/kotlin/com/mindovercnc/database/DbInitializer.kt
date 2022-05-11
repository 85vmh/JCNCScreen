package com.mindovercnc.database

import com.mindovercnc.base.data.tools.ToolHolderType
import com.mindovercnc.database.entity.ToolHolderEntity
import com.mindovercnc.database.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import kotlin.random.Random

object DbInitializer {
    private const val DB_NAME = "LatheTools.db"

    operator fun invoke() {
        Database.connect(
            url = "jdbc:sqlite:$DB_NAME",
            driver = "org.sqlite.JDBC"
        )

        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(
                CuttingInsertTable, WorkpieceMaterialTable, FeedsAndSpeedsTable, LatheCutterTable, ToolHolderTable
            )
        }
        transaction {
            if (ToolHolderEntity.count() == 0L) {
                createDummyHolders()
            }
        }

    }

    private fun createDummyHolders() {
        val types = ToolHolderType.values()
        repeat(5) {
            ToolHolderEntity.new {
                holderNumber = it
                holderType = types.random()
                cutter = null
                clampingPosition = 0
                xOffset = Random.nextDouble()
                zOffset = Random.nextDouble()
            }
        }
    }
}