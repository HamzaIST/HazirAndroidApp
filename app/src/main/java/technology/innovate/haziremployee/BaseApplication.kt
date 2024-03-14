package technology.innovate.haziremployee

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import technology.innovate.haziremployee.ui.applyjobform.Questiondetail

@HiltAndroidApp
class BaseApplication : Application(), ActivityLifecycleCallbacks {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        registerActivityLifecycleCallbacks(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Companion.currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        Companion.currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        Companion.currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        //currentActivity = null;
    }

    override fun onActivityStopped(activity: Activity) {
        //currentActivity = null;
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        //currentActivity = null;
    }

    companion object {
        @get:Synchronized
        lateinit var instance: BaseApplication
            private set
        private var currentActivity: Activity? = null
    }

    object QuestionObj{
        var postcodeList=ArrayList<Questiondetail>()
        var selectedquestionid=ArrayList<Int>()
        var detailid:String?=""
    }

    object Jobfilter{
        var departmentid:String=""
        var designationid:String=""
    }
}