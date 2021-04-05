package com.johan.reigntest.di.module

import android.content.Context
import com.johan.reigntest.di.ApplicationContext
import com.johan.reigntest.di.scope.AppScope
import com.johan.reigntest.util.SessionManager
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

@Module(includes = [AppModule::class])
class RetrofitModule {
    companion object {
        private const val BASE_URL = "https://hn.algolia.com/api/v1/"
    }

    @AppScope
    @Provides
    fun provideInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @AppScope
    @Provides
    fun provideCacheFile(@ApplicationContext context: Context): File = File(context.cacheDir,"prueba")

    @AppScope
    @Provides
    fun provideCache(cacheFile: File) = Cache(cacheFile, 10 * 1024 * 1024) //10MB Cahe

    @AppScope
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache?): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
                    var request = chain.request()
                    val maxAge = 60

                    request = request.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "private, max-age=$maxAge")
                        .build()

                    chain.proceed(request)
                })
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    var request = chain.request()

                    if (SessionManager.instance?.hasNetwork() == false) {
                        val maxStale = 60 * 60 * 24 * 7

                        request = request.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                            .build()
                    }
                    chain.proceed(request)
                })
                .build()
    }

    @AppScope
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @AppScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gson)
                .client(okHttpClient)
                .build()
    }
}