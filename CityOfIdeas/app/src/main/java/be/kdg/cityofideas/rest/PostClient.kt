package be.kdg.cityofideas.rest

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.lang.Exception
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class PostClient {
    private var retrofit: Retrofit? = null
    private lateinit var client : OkHttpClient

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()
private fun getUnsafeOkHttpClient() : OkHttpClient {
    try {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

           override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

           override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val sslSocketFactory : SSLSocketFactory  = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true }
        return builder.build()


    }
    catch (e : Exception){
        throw RuntimeException(e)
    }
}

    fun getClient(baseUrl: String): Retrofit? {
            client = getUnsafeOkHttpClient()

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        return retrofit
    }
}