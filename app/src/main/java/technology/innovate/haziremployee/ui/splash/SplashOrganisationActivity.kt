package technology.innovate.haziremployee.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.databinding.ActivitySplashBinding
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.utility.SessionManager
import technology.innovate.haziremployee.utility.setStatusBarTranslucent

@AndroidEntryPoint
class SplashOrganisationActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTranslucent(true)
        SessionManager.init(this)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        SessionManager.user?.organisationLogo?.let {
            viewBinding.image.load(it) {
                crossfade(true)
                crossfade(800)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }, 2000)

    }
}