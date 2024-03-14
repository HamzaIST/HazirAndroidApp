package technology.innovate.haziremployee.di

import android.util.Log
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
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.BuildConfig
import technology.innovate.haziremployee.rest.endpoints.EmployeeEndpoint
import technology.innovate.haziremployee.utility.SessionManager
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val CONNECT_TIMEOUT_SECONDS = 15
    private const val WRITE_TIMEOUT_SECONDS = 15
    private const val READ_TIMEOUT_SECONDS = 40

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.connectTimeout(CONNECT_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(logging)
        }

        okHttpBuilder.addInterceptor { chain ->
            val original = chain.request()
            Log.e("response",original.toString())

            val httpUrl = original.url
            val url = httpUrl
                .newBuilder()
                .build()

            val requestBuilder = original.newBuilder().url(url);

            SessionManager.init(BaseApplication.instance)
            if (SessionManager.token != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + SessionManager.token)
                println("SessionManager token = ${SessionManager.token}")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }


        return okHttpBuilder.build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit.Builder): EmployeeEndpoint {
        return retrofit.build().create(EmployeeEndpoint::class.java)
    }

}
