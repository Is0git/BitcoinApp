package com.is0git.bitcoin.di.modules

import com.is0git.bitcoin.network.adapters.BpiAdapter
import com.is0git.bitcoin.network.services.BpiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.coindesk.com/"

    @Provides
    @ActivityRetainedScoped
    @JvmStatic
    fun getInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @ActivityRetainedScoped
    @JvmStatic
    fun getClient(interceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .build()


    @Provides
    @ActivityRetainedScoped
    @JvmStatic
    fun getMoshiConverterFactory(bpiAdapter: BpiAdapter): MoshiConverterFactory {
        val moshi = Moshi.Builder().add(bpiAdapter).build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @ActivityRetainedScoped
    @JvmStatic
    fun getRetrofit(client: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .client(client)
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @ActivityRetainedScoped
    @JvmStatic
    fun getBpiService(retrofit: Retrofit): BpiService {
        return retrofit.create(BpiService::class.java)
    }
}