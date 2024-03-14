package technology.innovate.haziremployee.ui.manager.Addnewjob

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.prerequiistquestionmodel.Data

//class SelectedQuestionAdaptor {
//}

class SelectedQuestionAdaptor(private val mList: ArrayList<Data>, private val selectedcardview: CardView) : RecyclerView.Adapter<SelectedQuestionAdaptor.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selectedprereqquestion, parent, false)


        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]


        Log.e("nbvcd",ItemsViewModel.question)

        // sets the image to the imageview from our itemHolder class

        BaseApplication.QuestionObj.selectedquestionid.add(mList[position].id)

        // sets the text to the textview from our itemHolder class
       holder.textView.text=ItemsViewModel.question
        holder.button.setOnClickListener {
            BaseApplication.QuestionObj.selectedquestionid.remove(mList[position].id)
            mList.remove(ItemsViewModel)
            if (mList.isEmpty())
            {
                selectedcardview.visibility=View.GONE
            }
            notifyDataSetChanged()
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.checkbox)
        val button:ImageView=itemView.findViewById(R.id.deletea)

    }
}