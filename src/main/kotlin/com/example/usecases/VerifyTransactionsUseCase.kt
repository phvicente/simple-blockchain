package com.example.usecases

import com.example.domain.entities.Transaction
import com.example.domain.repositories.TransactionRepository

class VerifyTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    fun execute(transactions: List<Transaction>): Boolean {
        for (transaction in transactions) {
            if (transaction.id.isBlank() || transaction.content.isBlank()) {
                return false
            }
            if (transactionRepository.isTransactionDuplicate(transaction)) {
                return false
            }
        }
        return true
    }
}