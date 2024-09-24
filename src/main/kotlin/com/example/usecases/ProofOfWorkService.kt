package com.example.usecases

import com.example.domain.entities.Block
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest

class ProofOfWorkService(
    private val difficulty: Int
) {
    fun mineBlock(block: Block): Block {
        var nonce = 0
        var hash = ""
        val target = "0".repeat(difficulty)
        val jsonData = Json.encodeToString(block.data)

        do {
            val dataToHash = "${block.index}${block.timestamp}${jsonData}${block.previousHash}$nonce"
            hash = sha256(dataToHash)
            nonce++
        } while (!hash.startsWith(target))

        return block.copy(hash = hash, nonce = nonce - 1)
    }

    private fun sha256(dataToHash: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(dataToHash.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}