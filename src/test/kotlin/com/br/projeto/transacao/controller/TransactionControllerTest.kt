package com.br.projeto.transacao.controller

import com.br.projeto.transacao.exception.FileException
import com.br.projeto.transacao.model.dto.TransactionRequestDTO
import com.br.projeto.transacao.model.vo.TransactionVO
import com.br.projeto.transacao.service.TransactionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(TransactionController::class)
internal class TransactionControllerTest {

    @MockBean
    private lateinit var transactionService: TransactionService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Dado uma requisicao, deve retornar um Mock gerado de acordo com o conjunto de dados da requisicao`() {

        val transactionRequestDTO = TransactionRequestDTO(1001, 4, 2023)

        val transactions =
            listOf(
                TransactionVO(
                    "Blababuebaba banuce",
                    123412321,
                    10
                )
            )

        val expectedTransactions = "[{\"descricao\":\"Blababuebaba banuce\",\"data\":123412321,\"valor\":10}]"

        Mockito.`when`(transactionService.getTransaction(transactionRequestDTO)).thenReturn(transactions)

        val result = mockMvc.perform(
            get("/v1/{id}/transacoes/{ano}/{mes}", 1001, 2023, 4)
                .contentType("application/json")
                .param("sendWelcomeMail", "true")
        ).andExpect(status().isOk)
            .andReturn()

        assertEquals(expectedTransactions, result.response.contentAsString)
    }

    @Test
    fun `Dado uma requisicao deve retornar um response de codigo 500`() {

        val transactionRequestDTO = TransactionRequestDTO(1001, 4, 2023)

        Mockito.`when`(transactionService.getTransaction(transactionRequestDTO))
            .thenThrow(FileException("IOException IOException on createFile(4.json)"))

        val expectedError = "{\"message\":\"IOException IOException on createFile(4.json)\",\"data\":[]}"

        val result = mockMvc.perform(
            get("/v1/{id}/transacoes/{ano}/{mes}", 1001, 2023, 4)
                .contentType("application/json")
                .param("sendWelcomeMail", "true")
        ).andExpect(status().isInternalServerError)
            .andReturn()

        assertEquals(expectedError, result.response.contentAsString)
    }

    @Test
    fun `Dado uma requisicao deve retornar um response de codigo 400 e mensagem 'Id nao pode ser menor que 1000'`() {

        val responseError = "{\"message\":\"Parametro(s) Invalido(s)\"," +
            "\"data\":[{\"field\":\"getTransaction.id\",\"message\":\"Id nao pode ser menor que 1000\"}]}"

        val result = mockMvc.perform(
            get("/v1/{id}/transacoes/{ano}/{mes}", 999, 2023, 1)
                .contentType("application/json")
                .param("sendWelcomeMail", "true")
        ).andExpect(status().isBadRequest)
            .andReturn()

        assertEquals(responseError, result.response.contentAsString)
    }
}
