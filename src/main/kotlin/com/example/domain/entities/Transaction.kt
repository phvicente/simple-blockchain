package com.example.domain.entities

import kotlinx.serialization.Serializable


@Serializable
data class Transaction(
    val id: String,
    val content: String
)
