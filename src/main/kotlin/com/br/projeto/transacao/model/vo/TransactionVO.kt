package com.br.projeto.transacao.model.vo

import com.br.projeto.transacao.model.bo.Transaction
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class TransactionVO(
    @JsonProperty("descricao")
    val description: String,
    @JsonProperty("data")
    val date: Long,
    @JsonProperty("valor")
    val value: Int
) {
    constructor(transaction: Transaction) : this(
        transaction.description,
        Timestamp.valueOf(transaction.date).time,
        transaction.value
    )
}
