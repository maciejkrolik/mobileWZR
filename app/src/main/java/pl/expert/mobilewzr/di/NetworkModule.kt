package pl.expert.mobilewzr.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
            .addConverterFactory(CsvConverterFactory())
            .build()
            .create(WZRService::class.java)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

}