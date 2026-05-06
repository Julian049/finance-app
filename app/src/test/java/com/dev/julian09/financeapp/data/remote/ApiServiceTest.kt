package com.dev.julian09.financeapp.data.remote

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    private lateinit var server: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        server = MockWebServer()

        apiService = Retrofit.Builder()
            .baseUrl(server.url("/")) // La URL dinámica del servidor mock
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun `getAllTransactions devuelve una lista de transacciones correctamente`() = runBlocking {
        // 1. Preparamos el JSON falso (Mocked Response) basado en tu TransactionDto
        val jsonResponse = """
            [
                {
                    "id": 1,
                    "title": "Pago Alquiler",
                    "value": 1200.0,
                    "type": false,
                    "description": "Mensualidad de enero",
                    "date": "2026-05-05",
                    "synced": true
                },
                {
                    "id": 2,
                    "title": "Sueldo",
                    "value": 3000.0,
                    "type": true,
                    "description": "Pago empresa",
                    "date": "2026-05-01",
                    "synced": false
                }
            ]
        """.trimIndent()

        // 2. Configuramos el servidor para que devuelva este JSON
        server.enqueue(MockResponse().setResponseCode(200).setBody(jsonResponse))

        // 3. Ejecutamos la llamada al método que queremos probar
        val result = apiService.getAllTransactions()

        // 4. Verificaciones (Assertions)[cite: 1]
        assertEquals(2, result.size)
        assertEquals("Pago Alquiler", result[0].title)
        assertEquals(3000.0, result[1].value, 0.0)
        assertEquals(false, result[1].synced)

        // Verificamos que la petición se hizo al endpoint correcto
        val request = server.takeRequest()
        assertEquals("/transactions", request.path)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}