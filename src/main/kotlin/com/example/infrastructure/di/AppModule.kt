package com.example.infrastructure.di

import com.example.data.rocksdb.RocksDBBlockRepository
import com.example.data.rocksdb.RocksDBTransactionRepository
import com.example.domain.repositories.BlockRepository
import com.example.domain.repositories.TransactionRepository
import com.example.usecases.AddBlockUseCase
import com.example.usecases.ProofOfWorkService
import com.example.usecases.ValidateChainUseCase
import com.example.usecases.VerifyTransactionsUseCase
import org.koin.dsl.module

val appModule = module {
    single<BlockRepository> { RocksDBBlockRepository() }
    single<TransactionRepository> { RocksDBTransactionRepository() }

    single { VerifyTransactionsUseCase(get()) }
    single { ValidateChainUseCase() }
    single { ProofOfWorkService(difficulty = 4) }
    single { AddBlockUseCase(get(), get(), get(), get()) }


}