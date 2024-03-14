package technology.innovate.haziremployee.ui.timecreaditrequest.timecreditlistdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.databinding.ActivityTimecreditlistdetailBinding
import technology.innovate.haziremployee.rest.entity.creditlistdetail.CreditlistdetailModel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.timecreaditrequest.TimecreditViewmodel
import technology.innovate.haziremployee.utility.DataState
import technology.innovate.haziremployee.utility.hide
import technology.innovate.haziremployee.utility.show
import technology.innovate.haziremployee.utility.showToast
import technology.innovate.haziremployee.utils.CustomDialog

//class Timecreditlistdetail : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_timecreditlistdetail)
//    }
//}
@AndroidEntryPoint
class Timecreditlistdetail : AppCompatActivity() {

    // private val viewModel: Jobtitleviewmodel by viewModels()
    private lateinit var viewBinding: ActivityTimecreditlistdetailBinding
    private val viewModel: TimecreditViewmodel by viewModels()


    var detailid:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_timecreditlistdetail)

        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Timecreditlistdetail, HomeActivity::class.java))
            finish()
        }

        detailid=intent.getStringExtra("Username")
        Log.e("dfghjkl",   BaseApplication.QuestionObj.detailid.toString())


        intent.getStringExtra("Username")?.let { Log.e("ertyuio", it) }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Timecreditlistdetail, HomeActivity::class.java))
                finish()
            }
        })

        viewBinding.materialToolbar.setOnClickListener {
            startActivity(Intent(this@Timecreditlistdetail, HomeActivity::class.java))
            finish()
        }




        loadAllPostsFromRemote(detailid)
    }

    private fun loadAllPostsFromRemote(id:String?) {
        if (id != null) {
            viewModel.timecreditdetail(id)
            viewModel.timecreditdetail.observe(this, postsObserver)
        }


    }

    private var postsObserver: Observer<DataState<CreditlistdetailModel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {

                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()

                    viewBinding.name.text=it.item.data.employee.name
                    viewBinding.status.text=it.item.data.status
                    viewBinding.creditminutes.text=it.item.data.creditsRequiredInMins.toString()
                    viewBinding.creditDate.text=it.item.data.date
                    viewBinding.usagetype.text=it.item.data.usageType
                    viewBinding.email.text=it.item.data.employee.companyEmail
                    viewBinding.reasontext.text=it.item.data.reason





                }
                is DataState.Error -> {

                    viewBinding.progressBar.hide()
                    this.showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {

                    viewBinding.progressBar.hide()
                    CustomDialog(this).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (this as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }


}