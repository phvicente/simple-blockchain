package com.example.domain.repositories

import com.example.domain.entities.Block

interface BlockRepository {
    fun getLatestBlock(): Block?
    fun addBlock(block: Block)
    fun getAllBlocks(): List<Block>
}