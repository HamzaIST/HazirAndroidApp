package technology.innovate.haziremployee.ui.applyjobform

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.databinding.CurrencylisttextBinding
import technology.innovate.haziremployee.rest.entity.currencylistmodel.Data


//class CurrencylistAdaptor {
//}

class CurrencylistAdaptor :
    RecyclerView.Adapter<CurrencylistAdaptor.CurrencylistViewHolder>() {

    class CurrencylistViewHolder(val binding: CurrencylisttextBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem.id == newItem.id
    }


    val differ = AsyncListDiffer(this, differCallback)

    private var onRowClickListener: ((Data) -> Unit)? = null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencylistViewHolder {
        val binding = CurrencylisttextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CurrencylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencylistViewHolder, position: Int) {
        val item = differ.currentList[position]



        holder.binding.apply {
            data = item

            Log.e("countrynames",item.currency)
            holder.binding.title.setText(item.currency)
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