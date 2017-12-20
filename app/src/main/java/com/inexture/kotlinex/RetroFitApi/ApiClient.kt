package com.inexture.kotlinex.RetroFitApi

import com.inexture.kotlinex.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Inexture on 10/27/2017.
 */

object ApiClient {
    //    val BASE_URL = "http://election.inexture.com/api/"
    val BASE_URL = "https://reqres.in/api/"
    private var retrofit: Retrofit? = null


    private// add logging interceptor if DEBUG build
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                val builder = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                if (BuildConfig.DEBUG) {
                    val client = OkHttpClient.Builder()
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    client.addInterceptor(loggingInterceptor)
                    builder.client(client.build())
                }

                retrofit = builder.build()
            }
            return retrofit
        }

    val service: ApiInterface
        get() = client!!.create(ApiInterface::class.java)


}
