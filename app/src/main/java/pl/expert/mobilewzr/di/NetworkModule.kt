package pl.expert.mobilewzr.di

import dagger.Module
import dagger.Provides
import pl.expert.mobilewzr.data.URLs
import pl.expert.mobilewzr.data.WZRService
import pl.expert.mobilewzr.data.converter.CsvConverterFactory
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideWZRService(): WZRService {
        return Retrofit.Builder()
            .baseUrl(URLs.SUBJECTS_DOMAIN)
            .addConverterFactory(CsvConverterFactory.create())
            .build()
            .create(WZRService::class.java)
    }
}