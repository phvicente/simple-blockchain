package com.example.data.rocksdb

import com.example.domain.entities.Block
import com.example.domain.repositories.BlockRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.rocksdb.Options
import org.rocksdb.RocksDB

class RocksDBBlockRepository : BlockRepository {

    private val db: RocksDB
    private val options: Options

    init {
        RocksDB.loadLibrary()
        options = Options().setCreateIfMissing(true)
        val dbPath = "rocksdb_data"
        db = RocksDB.open(options, dbPath)
    }

    override fun getLatestBlock(): Block? {
        val lastIndex = getLastIndex()
        return if (lastIndex >= 0) {
            getBlockByIndex(lastIndex)
        } else {
            null
        }
    }

    override fun addBlock(block: Block) {
        val indexKey = block.index.toString().toByteArray()
        val blockBytes = serializeBlock(block)
        db.put(indexKey, blockBytes)

        db.put("lastIndex".toByteArray(), block.index.toString().toByteArray())
    }

    override fun getAllBlocks(): List<Block> {
        val blocks = mutableListOf<Block>()
        val lastIndex = getLastIndex()
        for (i in 0..lastIndex) {
            val block = getBlockByIndex(i)
            if (block != null) {
                blocks.add(block)
            }
        }
        return blocks
    }

    private fun getLastIndex(): Int {
        val lastIndexBytes = db.get("lastIndex".toByteArray())
        return lastIndexBytes?.toString(Charsets.UTF_8)?.toIntOrNull() ?: -1
    }

    private fun getBlockByIndex(index: Int): Block? {
        val indexKey = index.toString().toByteArray()
        val blockBytes = db.get(indexKey)
        return if (blockBytes != null) {
            deserializeBlock(blockBytes)
        } else {
            null
        }
    }

    private fun serializeBlock(block: Block): ByteArray {
        return Json.encodeToString(block).toByteArray(Charsets.UTF_8)
    }

    private fun deserializeBlock(bytes: ByteArray): Block {
        return Json.decodeFromString(String(bytes, Charsets.UTF_8))
    }

    fun close() {
        db.close()
        options.close()
    }

}