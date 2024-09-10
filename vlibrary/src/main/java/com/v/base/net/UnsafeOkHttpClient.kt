package com.v.base.net

/**
 * author  : ww
 * desc    :
 * time    : 2024/6/27 19:39
 */
import android.util.Log
import com.v.log.util.logE
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.Arrays
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object UnsafeOkHttpClient {
    /**
     * 配置https证书
     * @param clientBuilder okHttpClient.builder
     * @param certificateIs 读取证书的InputStream
     */
    fun setCertificates(clientBuilder: OkHttpClient.Builder, certificateIs: InputStream) {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            val certificate = certificateFactory.generateCertificate(certificateIs)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("selfCA", certificate)
            certificateIs.close()
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException(
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                )
            }
            val trustManager = trustManagers[0] as X509TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager)
        } catch (e: Throwable) {
            e.logE("证书配置失败:${ Log.getStackTraceString(e)}")
        }
    }


}
