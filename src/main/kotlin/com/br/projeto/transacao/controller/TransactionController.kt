package com.br.projeto.transacao.controller

import com.br.projeto.transacao.model.dto.TransactionRequestDTO
import com.br.projeto.transacao.model.vo.TransactionVO
import com.br.projeto.transacao.service.TransactionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Positive

@Validated
@Controller
class TransactionController(private val transactionService: TransactionService) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/v1/{id}/transacoes/{year}/{mounth}")
    fun getTransaction(
        @Positive(message = "Id nao pode ser menor ou  a zero")
        @Max(message = "Id nao pode ser maior que 100.000", value = 100000)
        @Min(message = "Id nao pode ser menor que 1000", value = 1000)
        @PathVariable("id") id: Int,
        @Max(message = "Ano nao pode ser maior que 3000", value = 3000)
        @Min(message = "Ano nao pode ser menor que 1800", value = 1800)
        @Positive(message = "Ano nao pode ser menor ou  a zero")
        @PathVariable("year") ano: Int,
        @Max(message = "Mes nao pode ser maior que 12", value = 12)
        @Min(message = "Mes nao pode ser menor que 1", value = 1)
        @Positive(message = "Mes nao pode ser menor ou  a zero")
        @PathVariable("mounth") mes: Int
    ): ResponseEntity<List<TransactionVO>> {
        logger.info("getTransaction id: $id->")
        val response = transactionService.getTransaction(TransactionRequestDTO(id, mes, ano))
        logger.info("getTrasaction Success id: $id<-")
        return ResponseEntity.ok().body(response)
    }
}
