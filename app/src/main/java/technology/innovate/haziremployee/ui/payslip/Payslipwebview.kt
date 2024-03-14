package technology.innovate.haziremployee.ui.payslip

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.BuildConfig
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.databinding.ActivityPayslipwebviewBinding
import technology.innovate.haziremployee.utility.SessionManager
import technology.innovate.haziremployee.utility.hide
import technology.innovate.haziremployee.utility.show
import java.nio.charset.Charset

//class Payslipwebview : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_payslipwebview)
//    }
//}
@AndroidEntryPoint
class Payslipwebview : AppCompatActivity() {

    // private val viewModel: Jobtitleviewmodel by viewModels()
    private lateinit var viewBinding:ActivityPayslipwebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_payslipwebview)

//        viewBinding.materialToolbar.setNavigationOnClickListener {
//            startActivity(Intent(this@Payslipwebview, HomeActivity::class.java))
//            finish()
//        }

//        detailid=intent.getStringExtra("Username")
//        Log.e("dfghjkl",   BaseApplication.QuestionObj.detailid.toString())
//
//
//        intent.getStringExtra("Username")?.let { Log.e("ertyuio", it) }

        Log.e("profileid",SessionManager.profileid.toString())
        Log.e("profileid", BaseApplication.QuestionObj.detailid.toString())

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Payslipwebview, PayslipActivity::class.java))
                finish()
            }
        })

        viewBinding.materialToolbar.setOnClickListener {
            startActivity(Intent(this@Payslipwebview, PayslipActivity::class.java))
            finish()
        }



        getVolley()
    }


    @SuppressLint("SuspiciousIndentation")
    fun getVolley() {
        viewBinding.progressBar.show()

        val queue = Volley.newRequestQueue(this)
        // val url = "https://staging.dubaileading.technology/maccess-saas/api/public/api/admin/employeePayrollSlip?payroll_id=58&employee_id=8583"
        val url =
            BuildConfig.BASE_URL + "admin/employeePayrollSlip?payroll_id=" + BaseApplication.QuestionObj.detailid.toString() + "&employee_id=" + SessionManager.profileid.toString()

        val requestBody = ""
        val stringReq: StringRequest =
            object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->

                    Log.i("mylog", response)

                    val mimeType = "text/html"
                    val encoding = "UTF-8"
                    viewBinding.webView.loadDataWithBaseURL("", response, mimeType, encoding, "")

                    viewBinding.export.setOnClickListener {
                        createWebPrintJob(viewBinding.webView)
                    }
                    viewBinding.progressBar.hide()
                    viewBinding.export.visibility=View.VISIBLE
                },
                Response.ErrorListener { error ->
                    Log.i("mylog", "error = " + error)
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer " + SessionManager.token


                    return headers
                }

                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)
    }
    private fun createWebPrintJob(webView: WebView) {

        val printManager = this
            .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter("MyDocument")

        val jobName = getString(R.string.app_name) + " Print Test"

        printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }


}