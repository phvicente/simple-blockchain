package com.example.domain.entities

import kotlinx.serialization.Serializable


@Serializable
data class Block(
    val index: Int,
    val timestamp: Long,
    val data: List<Transaction>,
    val previousHash: String,
    var hash: String,
    var nonce: Int
)
