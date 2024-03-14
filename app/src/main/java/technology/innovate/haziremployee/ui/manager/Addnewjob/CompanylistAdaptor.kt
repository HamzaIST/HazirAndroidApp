package technology.innovate.haziremployee.ui.manager.Addnewjob

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.jobcompanymodel.Data

//class CompanylistAdaptor {
//}

class CompanylistAdaptor(
    ctx: Context,
    val result: List<Data?>
) : ArrayAdapter<Data>(ctx, 0, result) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.row_dropdown,
            parent,
            false
        )

        if (position == 0) {
            view.findViewById<TextView>(R.id.nameTextView).text = context.getString(R.string.select)
            view.findViewById<TextView>(R.id.nameTextView).setTextColor(Color.GRAY)
        } else {
            val bean = getItem(position - 1)
            view.findViewById<TextView>(R.id.nameTextView).text = bean!!.title
            view.findViewById<TextView>(R.id.nameTextView)
                .setTextColor(ContextCompat.getColor(context, R.color.bodyGrey))
        }

        return view
    }

    override fun getCount(): Int {
        return result.size.plus(1)
    }
}