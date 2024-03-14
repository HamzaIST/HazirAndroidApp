package technology.innovate.haziremployee.ui.interviewround

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.databinding.InterviewlistlayoutBinding
import technology.innovate.haziremployee.rest.entity.interviewroundlistmodel.DataX
import technology.innovate.haziremployee.ui.jobpost.Jobpostlistviewmodel


class InterviewroundlistAdaptor(var context: Context, var jobpostlistviewmodel: Jobpostlistviewmodel) :
    PagingDataAdapter<DataX, InterviewroundlistAdaptor.ItemViewHolder>(ItemDifferCallback()) {
    class ItemViewHolder(val binding: InterviewlistlayoutBinding) : RecyclerView.ViewHolder(binding.root)

    var productMarker: String = ""
    var selectedItem: Int = -0
    var market = ""

    var devicetoken = ""

    class ItemDifferCallback : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(
            oldItem: DataX,
            newItem: DataX
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DataX,
            newItem: DataX
        ): Boolean =
            oldItem.id == newItem.id

    }

    private var onRowClickListener: ((DataX) -> Unit)? = null
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
 

        Log.e("jobname",item!!.jobPostName)
        holder.binding.jobPostNameinterview.setText(item!!.jobPostName)
        holder.binding.interviewstatus.setText(item.interviewStatus)
        holder.binding.interviewfullname.setText(item.fullName)
        holder.binding.interviewname.setText(item.name)
        holder.binding.interviewtype.setText(item.interviewType)
        holder.binding.timeinterview.setText(item.interviewDateTime)
        holder.binding.Interviewround.setText(item.interviewRoundNo.toString())

        holder.binding.row.setOnClickListener {
                val intent = Intent(context, DetailInterviewlist::class.java)
                intent.putExtra("Username",item.id.toString())
                context.startActivity(intent)
                BaseApplication.QuestionObj.detailid=item.id.toString()
            }



    }

    fun onRowClickListener(listener: ((DataX) -> Unit)) {
        onRowClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val findNavController: NavController
        val binding =
            InterviewlistlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return ItemViewHolder(binding)
    }


}
//class InterviewroundlistAdaptor( private val context: Context, var dataList: List<DataX?>?) : RecyclerView.Adapter<InterviewroundlistAdaptor.AttendanceViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.interviewlistlayout, parent, false)
//
//        return AttendanceViewHolder(view)
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
//        val bean = dataList?.get(position)
//        //        Log.e("jobname",item!!.jobPostName)
//        try {
//            holder.jobpostname.setText(bean!!.jobPostName)
//            holder.intervewstatus.setText(bean.interviewStatus)
//            holder.interviewfullname.setText(bean.fullName)
//            holder.interviewwername.setText(bean.name)
//            holder.interviewtype.setText(bean.interviewType)
//            holder.interviewdate.setText(bean.interviewDateTime.toString())
//
//            holder.interviewround.setText(bean.interviewRoundNo.toString())
//            holder.row.setOnClickListener {
//                val intent = Intent(context, DetailInterviewlist::class.java)
//                intent.putExtra("Username",bean.id.toString())
//                context.startActivity(intent)
//                BaseApplication.QuestionObj.detailid=bean.id.toString()
//            }
//        }
//        catch (ex:Exception)
//        {
//            ex.printStackTrace()
//        }
//
//
//    }
//
//    override fun getItemCount(): Int {
//        return dataList?.size!!
//    }
//
//    fun addList(items: List<DataX?>?){
//        this.dataList = items
//        notifyDataSetChanged()
//    }
//
//
//    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var interviewfullname = itemView.findViewById<View>(R.id.interviewfullname) as TextView
//        var interviewwername = itemView.findViewById<View>(R.id.interviewname) as TextView
//        var interviewdate = itemView.findViewById<View>(R.id.timeinterview) as TextView
//        var intervewstatus = itemView.findViewById<View>(R.id.interviewstatus) as TextView
//        var jobpostname = itemView.findViewById<View>(R.id.job_post_nameinterview) as TextView
//        var interviewtype = itemView.findViewById<View>(R.id.interviewtype) as TextView
//        var interviewround = itemView.findViewById<View>(R.id.Interviewround) as TextView
//        var row = itemView.findViewById<View>(R.id.row) as LinearLayout
//
//    }
//}