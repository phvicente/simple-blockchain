package com.example.plugins

import com.example.domain.entities.Transaction
import com.example.domain.repositories.BlockRepository
import com.example.usecases.AddBlockUseCase
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting(blockRepository: BlockRepository, addBlockUseCase: AddBlockUseCase) {
    routing {
        get("/blocks") {
            val blocks = blockRepository.getAllBlocks()
            call.respond(blocks)
        }

        post("/blocks/add") {
            val transactions = call.receive<List<Transaction>>()
            val result = addBlockUseCase.execute(transactions)

            result.fold(
                onSuccess = { block ->
                    call.respond(block)
                },
                onFailure = { error ->
                    call.respondText("Erro ao adicionar bloco: ${error.message}", status = io.ktor.http.HttpStatusCode.BadRequest)
                }
            )
        }
    }
}

