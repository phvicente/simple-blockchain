package com.example.domain.repositories

import com.example.domain.entities.Transaction

interface TransactionRepository {

    fun getTransactionById(id: String): Transaction?
    fun addTransaction(transaction: Transaction)
    fun isTransactionDuplicate(transaction: Transaction): Boolean

}