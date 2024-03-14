package technology.innovate.haziremployee.ui.applyjobform

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.databinding.DialogSearchableTextBinding
import technology.innovate.haziremployee.rest.entity.countrylistmodel.Data

//class CountrylistAdaptor {
//}

class CountrylistAdaptor :
    RecyclerView.Adapter<CountrylistAdaptor.CountrylistViewHolder>() {

    class CountrylistViewHolder(val binding: DialogSearchableTextBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem.id == newItem.id
    }


    val differ = AsyncListDiffer(this, differCallback)

    private var onRowClickListener: ((Data) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountrylistViewHolder {
        val binding = DialogSearchableTextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountrylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountrylistViewHolder, position: Int) {
        val item = differ.currentList[position]



        holder.binding.apply {
            data = item

            Log.e("countrynames",item.title.toString())
            holder.binding.title.setText(item.title)
            row.setOnClickListener {
                onRowClickListener?.invoke(item)
            }


        }


    }

    override fun getItemCount(): Int = differ.currentList.size




    fun setOnRowClickListener(listener: ((Data) -> Unit)) {
        onRowClickListener = listener
    }



}