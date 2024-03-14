package technology.innovate.haziremployee.ui.applyjobform

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.rest.entity.jobquestions.Data

//class JobQuestionAdaptor {
//}



class JobQuestionAdaptor(var context: Context, var dataList: List<Data>) : RecyclerView.Adapter<JobQuestionAdaptor.NotificationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobQuestionAdaptor.NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobquestionlayout, parent, false)

        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobQuestionAdaptor.NotificationViewHolder, position: Int) {
        val bean: Data = dataList?.get(position)!!
        val builder = StringBuilder()
        holder.notification.text = dataList?.get(position)?.question
        try {
            if (dataList?.get(position)?.questionOptions.isNullOrEmpty())
            {

                holder.answerview.visibility=View.VISIBLE
                BaseApplication.QuestionObj.postcodeList.add(Questiondetail(dataList?.get(position)!!.question,holder.answertextview.text.trim().toString()))


            }

            holder.answertextview.addTextChangedListener(object : TextWatcher {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun afterTextChanged(s: Editable?) {
                    BaseApplication.QuestionObj.postcodeList.toSet()
                    for(i in 0..BaseApplication.QuestionObj.postcodeList.size)
                    {
                        try {
                            if (BaseApplication.QuestionObj.postcodeList[i].question==dataList?.get(position)!!.question)
                            {
                                BaseApplication.QuestionObj.postcodeList.removeAt(i)
                            }
                        }
                        catch (ex:Exception)
                        {
                            ex.printStackTrace()
                        }

                    }
                    //BaseApplication.QuestionObj.postcodeList.removeAt(position)
                    BaseApplication.QuestionObj.postcodeList.add(Questiondetail(dataList?.get(position)!!.question,holder.answertextview.text.trim().toString()))
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
//            holder.answertextview.setOnFocusChangeListener { view, b ->
//                BaseApplication.QuestionObj.postcodeList.add(Questiondetail(dataList?.get(position)!!.question,holder.answertextview.text.trim().toString()))
//
//            }



//            for (i in 0 until dataList.get(position).questionOptions.size)
//            {
//            holder.checkedTextView.text=dataList.get(position).questionOptions[i]
//            }

         val arrayAdapter = CheckboxAdaptor(context, dataList.get(position).questionOptions,dataList?.get(position)?.question.toString())
            holder.checkedTextView.adapter=arrayAdapter

        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


        }
    }
    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    fun addList(notificationData : List<Data>){
        this.dataList = notificationData
        notifyDataSetChanged()
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notification = itemView.findViewById<View>(R.id.question) as TextView
        var checkedTextView = itemView.findViewById<View>(R.id.checkbox) as RecyclerView
        var answerview = itemView.findViewById<View>(R.id.answerview) as CardView
        var answertextview = itemView.findViewById<View>(R.id.answertextview) as EditText

    }
}
