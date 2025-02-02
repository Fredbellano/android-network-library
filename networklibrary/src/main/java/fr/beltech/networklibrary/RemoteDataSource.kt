package fr.beltech.networklibrary

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.gson.gson


class RemoteDataSource {
    companion object {
        private const val TAG = "RemoteDataSource"

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    serializeNulls()
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d(TAG, message)
                    }
                }
                level = LogLevel.INFO
            }

            install(ResponseObserver) {
                onResponse { response ->
                    //Log.d(TAG, " ${response.status.value}")
                }
            }

            install(DefaultRequest) {
                // Ajoute un header par défaut pour toutes les requêtes
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

    }
}

suspend inline fun <reified R> HttpClient.get(
    url: String,
    queryParams: Map<String, String> = mapOf(),
    headers: Map<String, Any> = mapOf()
): Result<R> =
    try {
        val result = this.get {
            url {
                url(url)
                queryParams.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
            headers {
                headers.forEach { (key, value) ->
                    append(key, value.toString())
                }
            }
        }

        if (result.status.value == 200) {
            Result.success(result.body())
        } else {
            Result.failure(Exception("Erreur ${result.status.value}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }


suspend inline fun <reified R> HttpClient.post(
    url: String,
    body: Any,
    queryParams: Map<String, String> = mapOf(),
    headers: Map<String, Any> = mapOf(),
): Result<R> =
    try {
        val result = this.post {
            url {
                url(url)
                queryParams.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
            setBody(body)
            headers {
                headers.forEach { (key, value) ->
                    append(key, value.toString())
                }
            }
        }

        if (result.status.value == 200) {
            Result.success(result.body())
        } else {
            Result.failure(Exception("Erreur ${result.status.value}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }


suspend inline fun <reified R> HttpClient.put(
    url: String,
    body: Any,
    queryParams: Map<String, String> = mapOf(),
    headers: Map<String, Any> = mapOf(),
): Result<R> = try {
    val result = this.put {
        url {
            url(url)
            queryParams.forEach { (key, value) ->
                parameters.append(key, value)
            }
        }
        setBody(body)
        headers {
            headers.forEach { (key, value) ->
                append(key, value.toString())
            }
        }
    }

    if (result.status.value == 200) {
        Result.success(result.body())
    } else {
        Result.failure(Exception("Erreur ${result.status.value}"))
    }
} catch (e: Exception) {
    Result.failure(e)
}

suspend inline fun <reified R> HttpClient.delete(
    url: String,
    queryParams: Map<String, String> = mapOf(),
    headers: Map<String, Any> = mapOf(),
): Result<R> = try {
    val result = this.delete {
        url {
            url(url)
            queryParams.forEach { (key, value) ->
                parameters.append(key, value)
            }
        }
        headers {
            headers.forEach { (key, value) ->
                append(key, value.toString())
            }
        }
    }

    if (result.status.value == 200) {
        Result.success(result.body())
    } else {
        Result.failure(Exception("Erreur ${result.status.value}"))
    }
} catch (e: Exception) {
    Result.failure(e)
}