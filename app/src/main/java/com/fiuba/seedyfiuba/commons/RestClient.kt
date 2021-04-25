package com.fiuba.seedyfiuba.commons

import com.fiuba.seedyfiuba.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {
	private var retrofit: Retrofit? = null


	private const val DEAULT_TIMEOUT = 20
	private const val MORE_TIMEOUT = 47

	/**
	 * Creates a interceptor to add or replace headers
	 */
	private fun createInterceptorHeaders(headers: Map<String, String>): Interceptor {
		return Interceptor { chain ->
			val builder = chain.request().newBuilder()
			for ((key, value) in headers) {
				builder.header(key, value)
			}
			chain.proceed(builder.build())
		}
	}

	private fun getClient(timeout: Int, headers: Map<String, String>): OkHttpClient {
		val clientBuilder = OkHttpClient.Builder()

		if (BuildConfig.DEBUG) {
			val interceptor = HttpLoggingInterceptor()
			interceptor.level = HttpLoggingInterceptor.Level.BODY
			clientBuilder.addInterceptor(interceptor)
		}

		if (headers.isNotEmpty()) {
			clientBuilder.addInterceptor(
				createInterceptorHeaders(
					headers
				)
			)
		}
		return clientBuilder
			.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
			.connectTimeout(
				timeout.toLong(),
				TimeUnit.SECONDS
			)
			.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
			.build()
	}

	private fun getGson(): Gson {
		return GsonBuilder()
			.setLenient()
			.create()
	}

	fun <S> getService(
		serviceClass: Class<S>,
		url: String = BASE_URL,
		timeout: Int = DEAULT_TIMEOUT,
		headers: Map<String, String> = mapOf()
	): S {
		val gson = getGson()
		val okHttpClient =
			getClient(timeout, headers)
		if (retrofit == null) {
			retrofit = Retrofit.Builder()
				.baseUrl(url)
				.client(okHttpClient)
				.addConverterFactory(
					GsonConverterFactory.create(
						gson
					)
				)
				.build()
		}

		return retrofit!!.create(serviceClass)
	}
}
