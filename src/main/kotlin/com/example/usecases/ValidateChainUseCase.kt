package com.example.usecases

import com.example.domain.entities.Block
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest

class ValidateChainUseCase {
    fun execute(blocks: List<Block>): Boolean {
        for (i in 1 until blocks.size) {
            val currentBlock = blocks[i]
            val previousBlock = blocks[i - 1]

            if (currentBlock.hash != calculateHash(currentBlock)) {
                return false
            }

            if (currentBlock.previousHash != previousBlock.hash) {
                return false
            }
        }
        return true
    }

    private fun calculateHash(block: Block): String {
        val jsonData = Json.encodeToString(block.data)
        val dataToHash = "${block.index}${block.timestamp}${jsonData}${block.previousHash}${block.nonce}"
        return sha256(dataToHash)
    }

     private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}