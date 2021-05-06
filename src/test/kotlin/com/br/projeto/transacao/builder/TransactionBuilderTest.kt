package com.br.projeto.transacao.builder

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Month

@ExtendWith(MockKExtension::class)
class TransactionBuilderTest {

    @InjectMockKs
    private lateinit var transactionBuilder: TransactionBuilder

    @Test
    fun `Dado uma requisicao deve retornar uma lista de transacoes`() {

        val transactions = transactionBuilder.buildTransactions(1001, 4, 2023)

        assertEquals(transactions.size, 4)
        transactions.forEach {
            assertTrue {
                it.value < 9999999 && it.value > -9999999
            }
            assertTrue {
                it.date.year == 2023 && it.date.month == Month.APRIL
            }
        }
    }
}
