package com.br.projeto.transacao.model.bo

data class TransactionGroup(
    val id: Int,
    val transactions: List<Transaction>
)
