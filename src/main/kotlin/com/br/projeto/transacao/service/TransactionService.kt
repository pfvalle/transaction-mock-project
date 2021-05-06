package com.br.projeto.transacao.service

import com.br.projeto.transacao.builder.TransactionBuilder
import com.br.projeto.transacao.exception.FileException
import com.br.projeto.transacao.model.bo.TransactionGroup
import com.br.projeto.transacao.model.dto.TransactionRequestDTO
import com.br.projeto.transacao.model.vo.TransactionVO
import com.br.projeto.transacao.util.FileUtils.buildFileJsonPath
import com.br.projeto.transacao.util.FileUtils.createJsonFile
import com.br.projeto.transacao.util.FileUtils.getObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class TransactionService(private val transactionBuilder: TransactionBuilder) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Throws(FileException::class)
    fun getTransaction(
        transactionRequestDTO: TransactionRequestDTO
    ): List<TransactionVO> {
        logger.info("Get transaction -> id: ${transactionRequestDTO.id}")

        val id = transactionRequestDTO.id
        val fileName = transactionRequestDTO.month.toString()
        val year = transactionRequestDTO.year
        val path = buildFileJsonPath(id, year).toString()

        return getObject<TransactionGroup>(fileName, path)
            ?.transactions?.map {
                TransactionVO(it)
            } ?: let {
            val transactions = transactionBuilder.buildTransactions(id, transactionRequestDTO.month, year)
            val group = TransactionGroup(id, transactions)
            createJsonFile(group, fileName, path)
            group.transactions.map {
                TransactionVO(it)
            }
        }
    }
}
