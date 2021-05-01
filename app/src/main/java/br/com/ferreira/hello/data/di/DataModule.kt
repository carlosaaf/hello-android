package br.com.ferreira.hello.data.di

import br.com.ferreira.hello.BuildConfig
import br.com.ferreira.hello.data.client.AuthApiService
import br.com.ferreira.hello.data.client.ProfileApiService
import br.com.ferreira.hello.data.util.AuthenticationHeaderInterceptor
import br.com.ferreira.hello.data.util.OAuth2Authenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class AnonOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class AuthOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class AuthService

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class HelloService

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    @AnonOkHttpClient
    fun provideOkHttpClient() =
        if (BuildConfig.DEBUG) { // debug ON
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()
        } else
            OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()

    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provicesAuthOkHttpClient(
        @AnonOkHttpClient okHttpClient: OkHttpClient,
        authenticationHeaderInterceptor: AuthenticationHeaderInterceptor,
        oauth2Autehnticator: OAuth2Authenticator
    ) =
        okHttpClient.newBuilder()
            .addInterceptor(authenticationHeaderInterceptor)
            .authenticator(oauth2Autehnticator)
            .build()

    @Provides
    @Singleton
    @AuthService
    fun provideAuthRetrofit(@AnonOkHttpClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @HelloService
    fun provideHelloRetrofit(@AuthOkHttpClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthApiService(@AuthService retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(@HelloService retrofit: Retrofit): ProfileApiService =
        retrofit.create(ProfileApiService::class.java)
}