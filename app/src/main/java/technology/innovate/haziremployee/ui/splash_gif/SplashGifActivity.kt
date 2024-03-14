package technology.innovate.haziremployee.ui.splash_gif

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.databinding.ActivitySplashGifBinding
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.login.LoginActivity
import technology.innovate.haziremployee.utility.SessionManager
import technology.innovate.haziremployee.utility.setStatusBarTranslucent


@AndroidEntryPoint
class SplashGifActivity : AppCompatActivity() {
    private var appUpdateManager: AppUpdateManager? = null
    private var binding: ActivitySplashGifBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(this)
        setStatusBarTranslucent(true)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_gif)
        appUpdateManager = AppUpdateManagerFactory.create(this)

        checkUpdate()

//        val gifFromAssets = GifDrawable(assets, "splash_gif.gif")
//        binding?.splashLogoImageView?.setImageDrawable(gifFromAssets)

    }

    private fun checkUpdate() {
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                navToHome()
            } else {
                navToHome()
            }
        }.addOnFailureListener {
            navToHome()
        }
    }




    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        try {
            val starter =
                IntentSenderForResultStarter {
                        intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
                    val request = IntentSenderRequest.Builder(intent)
                        .setFillInIntent(fillInIntent)
                        .setFlags(flagsValues, flagsMask)
                        .build()

                    activityResultLauncher.launch(request)
                }

            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                starter,
                IMMEDIATE_APP_UPDATE_REQ_CODE
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(),
        ) { result ->
            val resultCode = result.resultCode
            if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(
                    applicationContext,
                    "Update required", Toast.LENGTH_LONG
                ).show()
                finish()
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(
                    applicationContext,
                    "Successfully Updated!", Toast.LENGTH_LONG
                ).show()
                navToHome()
            }
            else
            {
                Toast.makeText(
                    applicationContext,
                    "Update Failed!", Toast.LENGTH_LONG
                ).show()
                checkUpdate()
            }

        }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
//            if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(
//                    applicationContext,
//                    "Update required", Toast.LENGTH_LONG
//                ).show()
//                finish()
//            } else if (resultCode == RESULT_OK) {
//                Toast.makeText(
//                    applicationContext,
//                    "Successfully Updated!", Toast.LENGTH_LONG
//                ).show()
//                navToHome()
//            } else {
//                Toast.makeText(
//                    applicationContext,
//                    "Update Failed!", Toast.LENGTH_LONG
//                ).show()
//                checkUpdate()
//            }
//        }
//    }

    @SuppressLint("SuspiciousIndentation")
    private fun navToHome() {

        val timeSettings = Settings.System.getString(
            contentResolver,
            Settings.Global.AUTO_TIME
        )
        Log.e("timesetting",timeSettings)
        val timeZone= Settings.System.getString(contentResolver, Settings.System.AUTO_TIME_ZONE)

        if(timeSettings!!.contentEquals("0")) {

            val builder = let { AlertDialog.Builder(it) }
            //set title for alert dialog
            builder.setTitle("Alert")
            //set message for alert dialog
            builder.setMessage("Please update Date Time settings as automatically")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
                finish();
                System.exit(0);


            }
            //performing cancel action
            builder.setNeutralButton("Cancel"){dialogInterface , which ->
                Toast.makeText(this,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        else if (timeZone!!.contentEquals("0"))
        {
            val builder = let { AlertDialog.Builder(it) }
            //set title for alert dialog
            builder.setTitle("Alert")
            //set message for alert dialog
            builder.setMessage("Please update Timezone settings as automatically")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
                finish();
                System.exit(0);

            }
            //performing cancel action


            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        else
        {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                if (SessionManager.isLoggedIn == true) {
                    //startActivity(Intent(applicationContext, SplashOrganisationActivity::class.java))
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                } else {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
                finish()

            }, 2000)

        }

    }

    companion object {
        private const val IMMEDIATE_APP_UPDATE_REQ_CODE = 124
    }

}
