package technology.innovate.haziremployee.ui.attendance

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.DataItem

class AttendanceReportAdapter( private val context: Context, var dataList: List<DataItem?>?) : RecyclerView.Adapter<AttendanceReportAdapter.AttendanceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_log, parent, false)

        return AttendanceViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val bean = dataList?.get(position)
        holder.dateTime.text = bean?.dateTimeUnix
        if (bean?.remarks != null){
            holder.remark.text = bean.remarks.toString()
        }
        holder.comp.text = bean?.sources.toString()

//        Log.e("jobtittle",bean!!.leaveData!!.title.toString())
        try {
            holder.title.text=bean!!.leaveData!!.title.toString()
            if (bean!!.leaveData!!.title.isNullOrEmpty())
            {
                holder.title.visibility=View.GONE
            }
            else{
                holder.title.visibility=View.VISIBLE
            }
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }

        if (bean?.mode!! == "in"){
            holder.status_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_break_in))
            holder.status.text = "Break-In"
        } else if (bean.mode == "out"){
            holder.status_img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_break_out))
            holder.status.text = "Break-Out"
        }  else if (bean.mode == "check-in"){
            holder.status.text = "Check-In"
        }else if (bean.mode == "check-out"){
            holder.status.text = "Check-Out"
        }

    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    fun addList(items: List<DataItem?>?){
        this.dataList = items
        notifyDataSetChanged()
    }


    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var status = itemView.findViewById<View>(R.id.status) as TextView
        var dateTime = itemView.findViewById<View>(R.id.dateTime) as TextView
        var status_img = itemView.findViewById<View>(R.id.status_img) as ImageView
        var remark = itemView.findViewById<View>(R.id.remark) as TextView
        var comp = itemView.findViewById<View>(R.id.comp) as TextView
        var title = itemView.findViewById<View>(R.id.projecttitle) as TextView
    }
}