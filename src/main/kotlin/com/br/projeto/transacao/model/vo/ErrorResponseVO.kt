package com.br.projeto.transacao.model.vo

data class ErrorResponseVO(
    val message: String,
    val data: List<ErrorVO>
)
