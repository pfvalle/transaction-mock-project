package com.br.projeto.transacao.service

import com.br.projeto.transacao.builder.TransactionBuilder
import com.br.projeto.transacao.model.bo.Transaction
import com.br.projeto.transacao.model.bo.TransactionGroup
import com.br.projeto.transacao.model.dto.TransactionRequestDTO
import com.br.projeto.transacao.model.vo.TransactionVO
import com.br.projeto.transacao.util.FileUtils
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.mockkObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.just
import io.mockk.Runs
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class TransactionServiceTest {

    @InjectMockKs
    private lateinit var transactionService: TransactionService

    @MockK
    private lateinit var transactionBuilder: TransactionBuilder

    @BeforeEach
    fun setUp() {
        mockkObject(FileUtils)
    }

    @Test
    fun `Dado uma requisicao que ja tenha transacoes salvas, deve retorna-las`() {

        val mockObjectMapper = mockk<ObjectMapper>(relaxed = true)
        val transactionRequestDTO = TransactionRequestDTO(1001, 4, 2023)

        val group = TransactionGroup(
            id = 1001,
            transactions = listOf(
                Transaction(
                    description = "Teste 123",
                    LocalDateTime.now(),
                    10
                )
            )
        )

        val expectedTransactionsVO = group.transactions.map {
            TransactionVO(it)
        }

        every { FileUtils.objectMapper } returns mockObjectMapper

        every {
            mockObjectMapper.readValue(
                any<File>(), TransactionGroup::class.java
            )
        } returns group

        val transactionsVO = transactionService.getTransaction(transactionRequestDTO)

        verify {
            mockObjectMapper.readValue(
                any<File>(), TransactionGroup::class.java
            )
        }

        assertEquals(expectedTransactionsVO, transactionsVO)
    }

    @Test
    fun `Dado uma requisicao que nao tenha arquivos de transacoes, deve criar o arquivo e retorna-lo`() {

        val mockObjectMapper = mockk<ObjectMapper>(relaxed = true)
        val transactionRequestDTO = TransactionRequestDTO(1001, 4, 2023)
        val builtTransactionList = listOf(
            Transaction(
                description = "Teste 123",
                LocalDateTime.now(),
                10
            )
        )
        val expectedTransactionsVO = builtTransactionList.map {
            TransactionVO(it)
        }

        every { FileUtils.objectMapper } returns mockObjectMapper

        every {
            mockObjectMapper.readValue(
                any<File>(),
                TransactionGroup::class.java
            )
        } throws FileNotFoundException()

        every {
            transactionBuilder.buildTransactions(1001, 4, 2023)
        } returns builtTransactionList

        every {
            FileUtils.createPath(any())
        } just Runs

        every {
            mockObjectMapper.writeValue(
                any<File>(),
                TransactionGroup::class.java
            )
        } just Runs

        val transactionsVO = transactionService.getTransaction(transactionRequestDTO)

        verify {
            mockObjectMapper.readValue(
                any<File>(),
                TransactionGroup::class.java
            )
        }

        assertEquals(expectedTransactionsVO, transactionsVO)
    }
}
