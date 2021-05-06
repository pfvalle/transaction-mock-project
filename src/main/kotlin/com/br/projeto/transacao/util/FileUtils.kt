package com.br.projeto.transacao.util

import com.br.projeto.transacao.exception.FileException
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.StringBuilder

const val RESOURCES_PATH = "\\src\\main\\resources"
const val DOT_JSON = ".json"
const val TRANSACTIONS = "transacoes"
const val BACKLASH = "\\"
const val PROJECT_PATH = "user.dir"

object FileUtils {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    val objectMapper = Jackson2ObjectMapperBuilder.json()
        .build<ObjectMapper>()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()

    fun buildFileJsonPath(id: Int, year: Int): StringBuilder =
        StringBuilder(System.getProperty(PROJECT_PATH))
            .append(RESOURCES_PATH)
            .append(BACKLASH)
            .append(id)
            .append(BACKLASH)
            .append(TRANSACTIONS)
            .append(BACKLASH)
            .append(year)

    fun buildFileJsonPathName(path: String, name: String): String =
        StringBuilder(path)
            .append(BACKLASH)
            .append(name)
            .append(DOT_JSON)
            .toString()

    fun createPath(path: String) {
        File(path).mkdirs()
        logger.info("Created path -> $path")
    }

    inline fun <reified T> createJsonFile(item: T, fileName: String, path: String) {
        try {
            createPath(path)
            objectMapper.writeValue(File(buildFileJsonPathName(path, fileName)), item)
            logger.info("Created file -> fileName: $fileName.json")
        } catch (e: IOException) {
            throw FileException("IOException IOException on createFile($fileName.json): ${e.message}")
        } catch (e: JsonGenerationException) {
            throw FileException("JsonGenerationException on createFile($fileName.json): ${e.message}")
        } catch (e: JsonMappingException) {
            throw FileException("JsonMappingException on createFile($fileName.json): ${e.message}")
        }
    }

    inline fun <reified T> getObject(fileName: String, path: String): T? {
        return try {
            logger.info("Get file -> fileName: $fileName.json - Path: $path")
            objectMapper.readValue(File(buildFileJsonPathName(path, fileName)), T::class.java)
        } catch (e: FileNotFoundException) {
            null
        } catch (e: IOException) {
            throw FileException("IOException IOException on getDataObject($fileName.json): ${e.message}")
        } catch (e: JsonGenerationException) {
            throw FileException("JsonGenerationException on getDataObject($fileName.json): ${e.message}")
        } catch (e: JsonMappingException) {
            throw FileException("JsonMappingException on getDataObject($fileName.json): ${e.message}")
        }
    }
}
