package com.br.projeto.transacao.model.bo

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Transaction(
    val description: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date: LocalDateTime,
    val value: Int
)
