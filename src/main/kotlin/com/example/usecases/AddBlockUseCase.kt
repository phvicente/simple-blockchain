package com.example.usecases

import com.example.domain.entities.Block
import com.example.domain.entities.Transaction
import com.example.domain.repositories.BlockRepository

class AddBlockUseCase(
    private val blockRepository: BlockRepository,
    private val verifyTransactionsUseCase: VerifyTransactionsUseCase,
    private val validateChainUseCase: ValidateChainUseCase,
    private val proofOfWorkService: ProofOfWorkService
) {
    fun execute(transactions: List<Transaction>): Result<Block> {
        val areTransactionsValid = verifyTransactionsUseCase.execute(transactions)
        if (!areTransactionsValid) {
            return Result.failure(Exception("Invalid transactions"))
        }

        val previousBlock = blockRepository.getLatestBlock()
        val newIndex = (previousBlock?.index ?: -1) + 1

        val newBlock = Block(
            index = newIndex,
            timestamp = System.currentTimeMillis(),
            data = transactions,
            previousHash = previousBlock?.hash ?: "0",
            hash = "",
            nonce = 0
        )

        val minedBlock = proofOfWorkService.mineBlock(newBlock)

        val currentChain = blockRepository.getAllBlocks() + minedBlock
        val isChainValid = validateChainUseCase.execute(currentChain)
        if (!isChainValid) {
            return Result.failure(Exception("Invalid Chain"))
        }

        blockRepository.addBlock(minedBlock)

        return Result.success(minedBlock)
    }
}