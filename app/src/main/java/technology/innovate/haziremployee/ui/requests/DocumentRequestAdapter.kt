package technology.innovate.haziremployee.ui.requests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.R

import technology.innovate.haziremployee.rest.entity.OtherRequestsItem

class DocumentRequestAdapter(
    val context: Context,
    private val documentClickListener: DocumentClickListener,
    var otherRequestsData: List<OtherRequestsItem?>
) :
    RecyclerView.Adapter<DocumentRequestAdapter.DocumentRequestsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentRequestsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.document_request_holder, parent, false)
        return DocumentRequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentRequestsViewHolder, position: Int) {
        holder.request_type.text = otherRequestsData[position]?.description
        holder.metadata.text = otherRequestsData[position]?.requesttype?.type

        holder.request_on.text = otherRequestsData[position]?.createdAt
        holder.status.text = otherRequestsData[position]?.statusId
        //holder.ref_number.text = otherRequestsData[position].reference_number

        if (otherRequestsData[position]?.statusId.equals("Pending")) {
            holder.status.background = ContextCompat.getDrawable(context, R.drawable.pending_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_yellow))
            holder.download.visibility = View.GONE
            holder.delete.visibility = View.VISIBLE
            //holder.edit.visibility = View.VISIBLE
        } else if (otherRequestsData[position]?.statusId.equals("Approved")) {
            holder.status.background = ContextCompat.getDrawable(context, R.drawable.approved_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_green))
            if (otherRequestsData[position]?.doc_url_from_admin != null) {
                holder.download.visibility = View.VISIBLE
            }
            holder.delete.visibility = View.GONE
            //holder.edit.visibility = View.GONE
        } else if (otherRequestsData[position]?.statusId.equals("Rejected")) {
            holder.status.background = ContextCompat.getDrawable(context, R.drawable.declined_bg)
            holder.status.setTextColor(context.resources.getColor(R.color.text_color_red))
            holder.delete.visibility = View.GONE
            holder.download.visibility = View.GONE
            //holder.edit.visibility = View.GONE
            /*if (otherRequestsData[position].doc_url_from_admin != null){
                holder.download.visibility = View.GONE
            }*/
        }

        holder.delete.setOnClickListener {
            documentClickListener.onDeleteClick(otherRequestsData[position]?.id!!)
        }

        holder.edit.setOnClickListener {
            documentClickListener.updateDocRequest(otherRequestsData[position]!!)
        }

        holder.download.setOnClickListener {
            documentClickListener.downloadDoc(otherRequestsData[position]!!)
        }

    }

    override fun getItemCount(): Int {
        return otherRequestsData.size
    }

    fun setData(list: List<OtherRequestsItem?>) {
        this.otherRequestsData = list
        notifyDataSetChanged()
    }

    class DocumentRequestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request_type = itemView.findViewById<View>(R.id.request_type) as TextView
        var metadata = itemView.findViewById<View>(R.id.metadata) as TextView
        var request_on = itemView.findViewById<View>(R.id.request_on) as TextView
        var status = itemView.findViewById<View>(R.id.status) as TextView

        //var ref_number = itemView.findViewById<View>(R.id.ref_number) as TextView
        var delete = itemView.findViewById<View>(R.id.delete) as ImageView
        var edit = itemView.findViewById<View>(R.id.edit) as ImageView
        var download = itemView.findViewById<View>(R.id.download) as ImageView
    }
}
