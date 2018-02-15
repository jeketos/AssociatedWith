package com.jeketos.associatedwith.rest

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jeketos.associatedwith.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


fun createRetrofit(): Retrofit {
    val clientBuilder = OkHttpClient.Builder()
//            getUnsafeOkHttpClientBuilder()
    clientBuilder.connectTimeout(15, TimeUnit.SECONDS)
    clientBuilder.writeTimeout(30, TimeUnit.SECONDS)
    clientBuilder.readTimeout(30, TimeUnit.SECONDS)
    clientBuilder.addInterceptor(getHeaderInterceptor())
    clientBuilder.addInterceptor {
        val response = it.proceed(it.request())
        if(!response.isSuccessful){
            throw Exception("server error ${response.code()} \n ${response.body()?.toString()}")
        }
        return@addInterceptor response
    }
    if (BuildConfig.DEBUG) clientBuilder.addInterceptor(getLoggingInterceptor())
    val okHttpClient = clientBuilder.build()
    val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.REST_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(JacksonConverterFactory.create(Json.createObjectMapper()))
            .client(okHttpClient)
            .build()
    return retrofit
}

private fun getHeaderInterceptor(): Interceptor {
    return Interceptor { chain ->
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
//                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
//        val token = context.getPreferences().getString(Constants.TOKEN, "")
//        if(token.isNotEmpty()){
//            requestBuilder.header("Authorization", "Token $token")
//        }
        chain.proceed(requestBuilder.build())
    }
}

private fun getLoggingInterceptor(): Interceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}