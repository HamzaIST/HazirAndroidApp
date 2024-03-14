package technology.innovate.haziremployee.ui.jobpostdetail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_jobpostdetail.*
import org.json.JSONObject
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.BuildConfig
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.databinding.ActivityJobpostdetailBinding
import technology.innovate.haziremployee.rest.entity.jobtitle.DataModel
import technology.innovate.haziremployee.ui.applyjobform.Applyjobform
import technology.innovate.haziremployee.ui.jobpost.JobPostAdaptor
import technology.innovate.haziremployee.ui.jobpost.Jobpost
import technology.innovate.haziremployee.utility.hide
import technology.innovate.haziremployee.utility.show
import java.util.ArrayList

@AndroidEntryPoint
class Jobpostdetail : AppCompatActivity() {

    // private val viewModel: Jobtitleviewmodel by viewModels()
    private lateinit var viewBinding: ActivityJobpostdetailBinding
    private lateinit var jobPostAdaptor: JobPostAdaptor
    internal lateinit var dataModelArrayList: ArrayList<DataModel>

    var detailid:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_jobpostdetail)

        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Jobpostdetail, Jobpost::class.java))
            finish()
        }

        detailid=intent.getStringExtra("Username")
        Log.e("dfghjkl",   BaseApplication.QuestionObj.detailid.toString())


        intent.getStringExtra("Username")?.let { Log.e("ertyuio", it) }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Jobpostdetail, Jobpost::class.java))
                finish()
            }
        })

        viewBinding.materialToolbar.setOnClickListener {
            startActivity(Intent(this@Jobpostdetail, Jobpost::class.java))
            finish()
        }

        viewBinding.share.setOnClickListener {
//            val openURL = Intent(android.content.Intent.ACTION_VIEW)
//            openURL.data = Uri.parse("https://staging.dubaileading.technology/maccess-saas/admin/admin/#!/open-job-post-view/"+BaseApplication.QuestionObj.detailid.toString())
//            startActivity(openURL)


            //val t1:String="https://dubaileading.technology/demo/maccess-saas/admin/admin/#!/open-job-post-view/"+BaseApplication.QuestionObj.detailid.toString()
           // val t1:String="https://staging.dubaileading.technology/maccess-saas/admin/admin/#!/open-job-post-view/"+BaseApplication.QuestionObj.detailid.toString()
            val t1:String="https://maccessweb.dubaileading.technology/admin/#!/open-job-post-view/"+BaseApplication.QuestionObj.detailid.toString()
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, t1)
            startActivity(Intent.createChooser(shareIntent,"Share via"))
        }


        getVolley()
    }


    @SuppressLint("SuspiciousIndentation")
    fun getVolley(){
        viewBinding.progressBar.show()

        val queue = Volley.newRequestQueue(this)
        val url: String = BuildConfig.BASE_URL+"admin/jobPostDetails/"+ BaseApplication.QuestionObj.detailid.toString()

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val obj = JSONObject(response)
                Log.e("responcesss",response.toString())
                dataModelArrayList = ArrayList()
                val dataArray = obj.getJSONObject("data")
                Log.e("responcesss",dataArray.getString("department_name"))

                viewBinding.progressBar.hide()

                viewBinding.jobPostName.text=dataArray.getString("job_post_name")
                viewBinding.experience.text=dataArray.getString("min_experience")+"-"+dataArray.getString("max_experience")+" Years"
                viewBinding.status.text=dataArray.getString("status")
                viewBinding.jobLocation.text=dataArray.getString("country")
                viewBinding.departmentName.text=dataArray.getString("department_name")
                viewBinding.designation.text=dataArray.getString("designation_name")
                viewBinding.nofpost.text=dataArray.getString("no_of_positions")
                viewBinding.responsibilty.text=dataArray.getString("job_responsibilities")
                viewBinding.description.text=dataArray.getString("job_description")
                viewBinding.salery.text=dataArray.getString("min_salary")+"-"+dataArray.getString("max_salary")+" "+dataArray.getString("currency")
                viewBinding.jobCategory.text=dataArray.getString("job_category")+"-"+dataArray.getString("job_type")

                    if(dataArray.getString("is_display_salary_job_page")=="Yes")
                    {
                        viewBinding.salarylayout.visibility=View.VISIBLE
                    }
                    else
                    {
                        viewBinding.salarylayout.visibility=View.GONE
                    }

                //

                viewBinding.submitMaterialButton.setOnClickListener {
                    val intent = Intent(this, Applyjobform::class.java)
                    intent.putExtra("id", dataArray.getString("id"))
                    intent.putExtra("Username",detailid)
                    startActivity(intent)
                }


            },
            { Log.d("API", "that didn't work") })
        queue.add(stringReq)
    }



}