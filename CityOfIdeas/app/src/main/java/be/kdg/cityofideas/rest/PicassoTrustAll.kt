package be.kdg.cityofideas.rest

import android.content.Context
import android.util.Log
import com.squareup.okhttp.OkHttpClient
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import java.security.cert.X509Certificate
import javax.net.ssl.*


class PicassoTrustAll {
    private var mInstance: Picasso? = null

    private fun PicassoTrustAll(context: Context) {
        val client = OkHttpClient()
    //    client.hostnameVerifier().verify(s,ssl)
        client.hostnameVerifier = HostnameVerifier { _, _ -> true }
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }


            @Throws(java.security.cert.CertificateException::class)
            override fun checkClientTrusted(
                x509Certificates: Array<java.security.cert.X509Certificate>,
                s: String
            ) {
            }

            @Throws(java.security.cert.CertificateException::class)
            override fun checkServerTrusted(
                x509Certificates: Array<java.security.cert.X509Certificate>,
                s: String
            ) {
            }
        })
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, java.security.SecureRandom())
            client.sslSocketFactory = sc.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mInstance = Picasso.Builder(context)
            .downloader(OkHttpDownloader(client) )
            .listener { _, _, exception -> Log.e("PICASSO", exception.toString()) }.build()

    }


    fun getInstance(context: Context): Picasso? {
        if (mInstance == null) {
            PicassoTrustAll(context)
        }
        return mInstance

}
}
