package com.example

import com.example.data.rocksdb.RocksDBBlockRepository
import com.example.data.rocksdb.RocksDBTransactionRepository
import com.example.domain.repositories.BlockRepository
import com.example.domain.repositories.TransactionRepository
import com.example.infrastructure.di.appModule
import com.example.plugins.configureRouting
import com.example.usecases.AddBlockUseCase
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {

   install(Koin) {
       slf4jLogger()
       modules(appModule)
   }

    install(ContentNegotiation) {
        json()
    }

    val blockRepository by inject<BlockRepository>()
    val addBlockUseCase by inject<AddBlockUseCase>()

    configureRouting(blockRepository, addBlockUseCase)

    environment.monitor.subscribe(ApplicationStopped) {
        if (blockRepository is RocksDBBlockRepository) {
            (blockRepository as RocksDBBlockRepository).close()
        }
        val transactionRepository by inject<TransactionRepository>()
        if (transactionRepository is RocksDBTransactionRepository) {
            (transactionRepository as RocksDBTransactionRepository).close()
        }
    }
}
