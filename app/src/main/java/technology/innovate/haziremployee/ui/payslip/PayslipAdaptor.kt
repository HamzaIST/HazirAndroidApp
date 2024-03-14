package technology.innovate.haziremployee.ui.payslip

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import technology.innovate.haziremployee.BuildConfig
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.paysliplistmodel.DataX
import technology.innovate.haziremployee.utility.SessionManager
import java.nio.charset.Charset
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class PayslipAdaptor(
    private val context: Context,
    var dataList: List<DataX?>?,
    progressBar: ProgressBar,
) : RecyclerView.Adapter<PayslipAdaptor.AttendanceViewHolder>() {
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payslipadaptorlayout, parent, false)
        bmp = BitmapFactory.decodeResource(context.resources, R.drawable.home_logo)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)
        return AttendanceViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val bean = dataList?.get(position)

        holder.processedate.text = monthnamegh(bean!!.toDate)
        val monthname:String="Payslip_"+monthnamegh(bean.toDate).toString().replace(" ","_")+"_"+SessionManager.loginname
        getVolley(bean.id.toString(),holder.webview,holder.row,monthname)

        try {
            val dob = bean.fromDate
            var month: String? = "0"
            var dd: String? = "0"
            var yer: String? = "0"
            try {

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val d = sdf.parse(dob)
                val cal = Calendar.getInstance()
                cal.time = d
                val monthss:Int = checkDigit(cal[Calendar.MONTH] + 1)!!.toInt()
                val monthname=getMonth(monthss)
                Log.e("dateconverter",monthname.toString())
                yer = checkDigit(cal[Calendar.YEAR])
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception){
           e.printStackTrace() // this never gets called either
        }



        holder.row.setOnClickListener {
            Log.e("datasadasdsd",SessionManager.loginname.toString())
            val datename:String="Payslip_"+monthnamegh(bean.toDate).toString().replace(" ","_")+"_"+SessionManager.loginname
            getVolley(bean.id.toString(), holder.webview,holder.row,datename)
        }

    }

    fun getMonth(month: Int): String? {
        return DateFormatSymbols().getMonths().get(month - 1)
    }
    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    fun addList(items: List<DataX?>?){
        this.dataList = items
        notifyDataSetChanged()
    }

    fun monthnamegh(convertdate:String): CharSequence? {

            var month: String? = "0"
            var dd: String? = "0"
            var yer: String? = "0"


                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val d = sdf.parse(convertdate)
                val cal = Calendar.getInstance()
                cal.time = d
                val monthss:Int = checkDigit(cal[Calendar.MONTH] + 1)!!.toInt()
                   val year:Int =checkDigit(cal[Calendar.YEAR])!!.toInt()
                val  monthname=getMonth(monthss)+" "+year
                Log.e("dateconverter",monthname.toString())
                yer = checkDigit(cal[Calendar.YEAR])

        return monthname
    }

    fun getVolley(detailid: String, webview: WebView, row: LinearLayout,monthname:String)
    {

        val queue = Volley.newRequestQueue(context)
        // val url = "https://staging.dubaileading.technology/maccess-saas/api/public/api/admin/employeePayrollSlip?payroll_id=58&employee_id=8583"
        val url = BuildConfig.BASE_URL + "admin/employeePayrollSlip?payroll_id=" + detailid + "&employee_id=" + SessionManager.profileid.toString()

        val requestBody = ""
        val stringReq: StringRequest =
            object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->

                    val mimeType = "text/html"
                    val encoding = "UTF-8"
                    webview.loadDataWithBaseURL("", response, mimeType, encoding, "")

                    row.setOnClickListener {
                        createWebPrintJob(webview,monthname.toString())
                    }

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


    private fun createWebPrintJob(webView: WebView,monthname: String) {

        val printManager = context
            .getSystemService(Context.PRINT_SERVICE) as PrintManager

        val printAdapter = webView.createPrintDocumentAdapter(monthname)

        Log.e("printname",monthname)
        val jobName = context.getString(R.string.app_name) + " Print Test"

        printManager.print(
            monthname, printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }
    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

          var processedate = itemView.findViewById<View>(R.id.processedate) as TextView
          var row = itemView.findViewById<View>(R.id.paysliprow) as LinearLayout
          var webview = itemView.findViewById<View>(R.id.web_view) as WebView


    }
}