package com.br.projeto.transacao.util

fun Int.getFirstDigit(): Int {
    return this.toString().substring(0, 1).toInt()
}
