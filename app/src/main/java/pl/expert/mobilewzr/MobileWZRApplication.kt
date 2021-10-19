package pl.expert.mobilewzr

import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import pl.expert.mobilewzr.di.DaggerMobileWZRComponent
import pl.expert.mobilewzr.di.DatabaseModule
import javax.inject.Inject

class MobileWZRApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerMobileWZRComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .build()
            .inject(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector

}