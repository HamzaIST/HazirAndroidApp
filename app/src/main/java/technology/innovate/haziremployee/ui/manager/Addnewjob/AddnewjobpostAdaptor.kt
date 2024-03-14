package technology.innovate.haziremployee.ui.manager.Addnewjob

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.databinding.PrereqquestionBinding
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Data

//class AddnewjobpostAdaptor {
//}

class AddnewjobpostAdaptor(private val requireContext: Context) :
    RecyclerView.Adapter<AddnewjobpostAdaptor.ItemViewHolder>() {
    class ItemViewHolder(val binding: PrereqquestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: PrereqquestionBinding =
            PrereqquestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem.id == oldItem.id

    }
    private var onRowClickListener: ((Data) -> Unit)? = null
    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            holder.binding.checkbox.text =item.question

            row.setOnClickListener{
                onRowClickListener?.invoke(item)
            }


        }



    }

    fun onRowClickListener(listener: ((Data) -> Unit)) {
        onRowClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}