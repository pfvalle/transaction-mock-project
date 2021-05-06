package com.br.projeto.transacao.builder

import com.br.projeto.transacao.model.bo.Transaction
import com.br.projeto.transacao.util.getFirstDigit
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@Component
class TransactionBuilder {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun buildTransactions(id: Int, month: Int, year: Int): List<Transaction> {

        val transactions = ArrayList<Transaction>()

        val numberTransactions = calculateNumberOfTransactions(id, month)

        for (item: Int in 0 until numberTransactions) {
            transactions.add(
                item,
                buildTransaction(id, year, month, item)
            )
        }

        logger.info("Built Transactions -> Id: $id size: ${transactions.size}")
        return transactions
    }

    private fun buildTransaction(id: Int, year: Int, month: Int, transactionIndex: Int): Transaction {
        logger.info("buildTransaction -> Id: $id")
        return Transaction(
            generateRandomDescription(),
            buildRandomDate(year, month),
            generateValue(id, transactionIndex, month)
        )
    }

    private fun calculateNumberOfTransactions(id: Int, month: Int): Int {
        val number = id.getFirstDigit() * month
        logger.info("calculateNumberOfTransactions -> Id: $id Number of Transactions: $number")
        return number
    }

    private fun generateValue(id: Int, index: Int, month: Int): Int {
        val value = (-9999999..9999999).random() / (id + index + month)
        logger.info("generateValue -> Value: $value")
        return value
    }

    private fun buildRandomDate(year: Int, month: Int): LocalDateTime {
        val lengthOfMonth = YearMonth.of(year, month).lengthOfMonth()
        val date = LocalDate.of(year, month, (1..lengthOfMonth).random())
        val localdatetime = LocalDateTime.of(date, LocalTime.now())
        logger.info("buildRandomDate -> date: $localdatetime")
        return localdatetime
    }

    private fun generateRandomDescription(): String {
        val descriptionLength = (10..60).random()
        val numberOfWords = descriptionLength / (5..10).random()
        val wordMaxLength = calculateWordMaxLength(descriptionLength, numberOfWords)

        val description = (1..numberOfWords).joinToString(" ") {
            val wordLength = (2..wordMaxLength).random()
            generateRandomString(wordLength)
        }
        logger.info("generateRandomDescription -> description: $description")
        return description
    }

    private fun calculateWordMaxLength(descriptionLength: Int, numberOfWords: Int): Int {
        val numberOfSpaces = calculateNumberOfSpaces(numberOfWords)
        return (descriptionLength - numberOfSpaces) / numberOfWords
    }

    private fun calculateNumberOfSpaces(numberOfWords: Int) = numberOfWords - 1

    private fun generateRandomString(length: Int): String {
        val vowels = "aeiou"
        val consonants = "bcdfghklmnpqrstvwxyz"
        return (1..length / 2).joinToString("") {
            "${consonants.random()}${vowels.random()}"
        }
    }
}
