package com.br.projeto.transacao.controller.advice

import com.br.projeto.transacao.exception.FileException
import com.br.projeto.transacao.model.vo.ErrorResponseVO
import com.br.projeto.transacao.model.vo.ErrorVO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ControllerExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val ERROR_MESSAGE_PARAMETER = "Parametro(s) Invalido(s)"
    }

    @ExceptionHandler(FileException::class)
    fun handleFileException(e: FileException): ResponseEntity<ErrorResponseVO> {
        logger.error("ExceptionHandler -> handleFileException -> ${e.message}")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseVO(e.message, emptyList()))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<ErrorResponseVO> {
        val error = ErrorResponseVO(
            message = ERROR_MESSAGE_PARAMETER,
            data = e.constraintViolations
                .map {
                    ErrorVO(
                        it.propertyPath.toString(),
                        it.messageTemplate
                    )
                }
        )
        logger.error("ExceptionHandler -> handleFileException -> ${e.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}
