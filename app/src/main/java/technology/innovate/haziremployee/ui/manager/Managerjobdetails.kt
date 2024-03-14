package technology.innovate.haziremployee.ui.manager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.databinding.ActivityManagerjobdetailsBinding
import technology.innovate.haziremployee.rest.entity.jobtitle.DataModel
import technology.innovate.haziremployee.rest.entity.managerjobdetailmodel.Mangerjobdetailmanagermodel
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.ui.applyjobform.JobQuestionViewModel
import technology.innovate.haziremployee.ui.jobpost.JobPostAdaptor
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog


@AndroidEntryPoint
class Managerjobdetails : AppCompatActivity() {

    // private val viewModel: Jobtitleviewmodel by viewModels()
    private lateinit var viewBinding: ActivityManagerjobdetailsBinding
    private lateinit var jobPostAdaptor: JobPostAdaptor
    internal lateinit var dataModelArrayList: ArrayList<DataModel>
    private val viewModel by viewModels<JobQuestionViewModel>()
    var detailid:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_managerjobdetails)

        viewBinding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Managerjobdetails, Managerjobpostlist::class.java))
            finish()
        }

        detailid=intent.getStringExtra("Username")
        Log.e("dfghjkl",   BaseApplication.QuestionObj.detailid.toString())


        intent.getStringExtra("Username")?.let { Log.e("ertyuio", it) }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Managerjobdetails, Managerjobpostlist::class.java))
                finish()
            }
        })

        viewBinding.materialToolbar.setOnClickListener {
            startActivity(Intent(this@Managerjobdetails, Managerjobpostlist::class.java))
            finish()
        }

        loadAllPostsFromRemote(detailid)


    //    getVolley()
    }
    private fun loadAllPostsFromRemote(id:String?) {
        if (id != null) {
            viewModel.managerjobdetail(id)
            viewModel.managerjobdetails.observe(this, postsObserver)
        }


    }

    private var postsObserver: Observer<DataState<Mangerjobdetailmanagermodel>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    Log.e("dataasd",it.item.data.toString())
                    viewBinding.jobPostName.text=it.item.data.jobPostName
                    viewBinding.experience.text=it.item.data.minExperience+"-"+it.item.data.maxExperience+" Years"
                    viewBinding.description.text=it.item.data.jobDescription
                    viewBinding.responsibilty.text=it.item.data.jobResponsibilities
                    viewBinding.designation.text=it.item.data.designation
                    viewBinding.salery.text=it.item.data.minSalary+"-"+it.item.data.maxSalary
                    viewBinding.status.text=it.item.data.jobRequestStatus
                    viewBinding.nofpost.text=it.item.data.noOfPositions.toString()


//                    val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
//                        this, android.R.layout.simple_list_item_1, it.item.data.preRequisitesQuestions.map { it.question }
//                    )
//                    viewBinding.recyclerView.adapter=arrayAdapter

                    for (i in 0 until it.item.data.preRequisitesQuestions.size) {
                        val text = TextView(this)
                        text.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        text.text = it.item.data.preRequisitesQuestions[i].question
                        viewBinding.llMain.addView(text)
                    }

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