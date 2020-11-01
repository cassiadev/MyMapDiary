package com.mmp.mymapdiary

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
//import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkMapsType() {
        assertTrue(getMaps() is List<SelectAllMapsQuery.Map?>?)
    }

    private fun getMaps() = runBlocking {

        // Create an ApolloClient with the project's endpoint
        val apolloClient = ApolloClient.builder()
            .serverUrl("https://5ahxbvmodj.execute-api.us-east-1.amazonaws.com/dev/graphql")
            .build()

        // Obtain response by synchronization in the coroutine scope
        val request = async<List<SelectAllMapsQuery.Map?>?> {
            val response = try {
                apolloClient.query(SelectAllMapsQuery()).toDeferred().await()
            } catch (e: ApolloException) {
                // handle protocol errors
                null!!
            }

            val maps = response.data?.maps
            if (maps == null || response.hasErrors()) {
                // handle application errors
                null!!
            }

            // The launched now contains type-safe model of the data
            println("maps: ${maps}")
            maps
        }

        val result = request.await()
        println("getMaps: ${result}")
        result
    }
}
