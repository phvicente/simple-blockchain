package com.example.data.rocksdb

import com.example.domain.entities.Transaction
import com.example.domain.repositories.TransactionRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.rocksdb.Options
import org.rocksdb.RocksDB

class RocksDBTransactionRepository : TransactionRepository {

    private val db: RocksDB
    private val options: Options

    init {
        RocksDB.loadLibrary()
        options = Options().setCreateIfMissing(true)
        val dbPath = "rocksdb_transactions"
        db = RocksDB.open(options, dbPath)
    }

    override fun getTransactionById(id: String): Transaction? {
        val transactionBytes = db.get(id.toByteArray(Charsets.UTF_8))
        return transactionBytes?.let {
            Json.decodeFromString(String(it, Charsets.UTF_8))
        }
    }

    override fun addTransaction(transaction: Transaction) {
        val transactionBytes = Json.encodeToString(transaction).toByteArray(Charsets.UTF_8)
        db.put(transaction.id.toByteArray(Charsets.UTF_8), transactionBytes)
    }

    override fun isTransactionDuplicate(transaction: Transaction): Boolean {
        return getTransactionById(transaction.id) != null
    }

    fun close() {
        db.close()
        options.close()
    }
}