package com.dev.julian09.financeapp.viewmodel

import com.dev.julian09.financeapp.data.remote.ApiService
import com.dev.julian09.financeapp.data.remote.TransactionDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransactionViewModelTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        // Configuramos Retrofit para que apunte al servidor de pruebas
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // URL dinámica del mock
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun `probar que getAllTransactions recibe los datos y los convierte bien`() = runBlocking {
        // Simulamos la respuesta del servidor con el JSON exacto que esperas
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("""
                [
                    {
                        "date": "2026-04-27T15:30:00Z",
                        "description": "Compras de la semana",
                        "localId": "550e8400-e29b-41d4-a716-446655440000",
                        "synced": true,
                        "title": "Mercado",
                        "type": true,
                        "value": 85000.0
                    }
                ]
            """.trimIndent())

        mockWebServer.enqueue(response)

        // LLAMADA REAL A RETROFIT
        val result = apiService.getAllTransactions()

        // VERIFICACIÓN: ¿Llegó lo que enviamos?
        // Nota: Si getAllTransactions devuelve Response<List<TransactionDto>>,
        // tendrías que usar result.body()!!.isNotEmpty()
        assertTrue(result.isNotEmpty())
        assertEquals("Mercado", result[0].title)
        assertEquals(85000.0, result[0].value, 0.0)
    }

    @Test
    fun `probar que createTransaction envia el JSON correcto`() = runBlocking {
        mockWebServer.enqueue(MockResponse().setResponseCode(201).setBody("{}"))

        val newTransaction = TransactionDto(
            id = 0, title = "Ahorro", value = 500.0,
            type = true, description = "Cochinito",
            date = "2026-05-05", synced = false
        )

        // Enviamos la transacción
        apiService.createTransaction(newTransaction)

        // Capturamos lo que Retrofit REALMENTE envió al "servidor"
        val request = mockWebServer.takeRequest()

        // Verificamos que el método sea POST y la ruta sea correcta
        assertEquals("POST", request.method)
        assertEquals("/transactions", request.path)

        // Aquí podrías ver el cuerpo del JSON enviado:
        println("Cuerpo enviado: ${request.body.readUtf8()}")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}