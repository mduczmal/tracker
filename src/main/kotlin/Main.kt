import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

@Serializable
data class Quote(val quoteResponse: QuoteResponse)

@Serializable
data class QuoteResponse(val result: List<Details>)

@Serializable
data class Details(val regularMarketPrice: Double)

private val format = Json { ignoreUnknownKeys = true }

fun callYahoo() {
    val client = OkHttpClient()

    val symbol = "AAPL"
    val request = Request.Builder()
        .url("https://yh-finance.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=$symbol")
        .get()
        .addHeader("x-rapidapi-host", "yh-finance.p.rapidapi.com")
        .addHeader("x-rapidapi-key", System.getenv("API_KEY"))
        .build()

    val response = client.newCall(request).execute()
    val body = response.body?.string() ?: throw RuntimeException("No response body")
    val json = format.decodeFromString<Quote>(body)
    println(json.quoteResponse.result[0].regularMarketPrice.toBigDecimal())
}


fun main(args: Array<String>) {
    callYahoo()
}