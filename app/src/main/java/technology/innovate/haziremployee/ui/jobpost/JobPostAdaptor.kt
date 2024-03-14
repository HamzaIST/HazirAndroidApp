package technology.innovate.haziremployee.ui.jobpost

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
import technology.innovate.haziremployee.databinding.JobtitleviewBinding
import technology.innovate.haziremployee.rest.entity.jobpostlistresponse.DataXX
import technology.innovate.haziremployee.ui.jobpostdetail.Jobpostdetail

//class JobPostAdaptor {
//}

 class JobPostAdaptor(var context: Context, var jobpostlistviewmodel: Jobpostlistviewmodel) :
    PagingDataAdapter<DataXX, JobPostAdaptor.ItemViewHolder>(ItemDifferCallback()) {
    class ItemViewHolder(val binding: JobtitleviewBinding) : RecyclerView.ViewHolder(binding.root)

    var productMarker: String = ""
    var selectedItem: Int = -0
    var market = ""

    var devicetoken = ""

    class ItemDifferCallback : DiffUtil.ItemCallback<DataXX>() {
        override fun areItemsTheSame(
            oldItem: DataXX,
            newItem: DataXX
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DataXX,
            newItem: DataXX
        ): Boolean =
            oldItem.detailId == newItem.detailId

    }

    private var onRowClickListener: ((DataXX) -> Unit)? = null
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        Log.e("joblistresponse",item?.jobCategory.toString())

        holder.binding.jobPostName.setText(item!!.jobPostName)
        holder.binding.jobCategory.setText(item.jobCategory+" "+item.jobType)
        holder.binding.jobLocation.setText(item.jobLocation)
        holder.binding.departmentName.setText(item.departmentName)
        holder.binding.experience.setText(item.minExperience+" - "+item.maxExperience +"Years")
        holder.binding.status.setText(item.status)

        holder.binding.row.setOnClickListener {
            val intent = Intent(context, Jobpostdetail::class.java)
            intent.putExtra("Username",item.detailId)
            context.startActivity(intent)
            BaseApplication.QuestionObj.detailid=item.detailId
        }



    }

    fun onRowClickListener(listener: ((DataXX) -> Unit)) {
        onRowClickListener = listener
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
         val findNavController: NavController
         val binding =
             JobtitleviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)


         return ItemViewHolder(binding)
     }


 }

