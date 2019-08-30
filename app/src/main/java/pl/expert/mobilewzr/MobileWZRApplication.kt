package pl.expert.mobilewzr

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.expert.mobilewzr.di.DaggerMobileWZRComponent
import pl.expert.mobilewzr.di.DatabaseModule
import javax.inject.Inject

class MobileWZRApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerMobileWZRComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .build()
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

}