package technology.innovate.haziremployee.ui.applyjobform

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import technology.innovate.haziremployee.BaseApplication
import technology.innovate.haziremployee.R
import java.lang.reflect.Type

//class CheckboxAdaptor {
//}
class CheckboxAdaptor(var context: Context, var dataList: List<String>,var question:String) : RecyclerView.Adapter<CheckboxAdaptor.NotificationViewHolder>() {

    private var selectedPosition = -1
    lateinit var courseList: ArrayList<Questiondetail>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxAdaptor.NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.optionlist, parent, false)
        courseList = ArrayList()
        return NotificationViewHolder(view)
    }
    private var setOnCheckedClickListener: ((String, View) -> Unit)? = null
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: CheckboxAdaptor.NotificationViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val bean: String = dataList.get(position)
        val builder = StringBuilder()
        val arrayAdapter: CheckboxAdaptor

        try {
            holder.checkedTextView.isChecked= selectedPosition==position
             holder.checkedTextView.text=bean


        Log.e("question",question)

             holder.checkedTextView.setOnCheckedChangeListener { compoundButton, b ->

          if(compoundButton.isChecked)
          {

              selectedPosition=position

              for(i in 0..BaseApplication.QuestionObj.postcodeList.size)
              {
                  try {
                      if (BaseApplication.QuestionObj.postcodeList[i].question==question)
                      {
                          BaseApplication.QuestionObj.postcodeList.removeAt(i)
                      }
                  }
                  catch (ex:Exception)
                  {
                      ex.printStackTrace()
                  }

              }


                  try {
                      BaseApplication.QuestionObj.postcodeList.add(Questiondetail(question,bean))
//                      for (i in 0..BaseApplication.QuestionObj.postcodeList.size)
//                      {
//                          Log.e("datasssss",BaseApplication.QuestionObj.postcodeList.toString())
//                      if(BaseApplication.QuestionObj.postcodeList[i].question==question)
//                      {
//                          BaseApplication.QuestionObj.postcodeList.removeAt(selectedPosition)
//                      }
                    //  }

//                      BaseApplication.QuestionObj.postcodeList.forEachIndexed { index, questiondetail ->
//                          Log.e("qwertyui",BaseApplication.QuestionObj.postcodeList[index].question)
//                          Log.e("qwertyuiwwwww",question)
//                          if (BaseApplication.QuestionObj.postcodeList[index].question.contains(question))
//                          {
//                              BaseApplication.QuestionObj.postcodeList.removeAt(index)
//                              BaseApplication.QuestionObj.postcodeList.add(Questiondetail(question,bean))
//                          }
//                          else
//                          {
//                              BaseApplication.QuestionObj.postcodeList.add(Questiondetail(question,bean))
//                          }
//                      }
                  }
                 catch (ex:Exception)
                 {
                     ex.printStackTrace()
                 }







//                courseList.add(Questiondetail(question,bean))
              val sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE)
              val editor = sharedPreferences.edit()

              // creating a new variable for gson.
              val gson = Gson()
              val jsons: String = gson.toJson(courseList)
///////////////////////////////////////////////////////////////



              // below line is to get the type of our array list.
              val type: Type = object : TypeToken<ArrayList<Questiondetail?>?>() {}.type

              // in below line we are getting data from gson
              // and saving it to our array list
              courseList = gson.fromJson<Any>(jsons, type) as ArrayList<Questiondetail>
//              Log.e("rtyuio",courseList[0].courseName.toString())




              ////////////////////////////////////////////////////////////////////////////////
              // creating a variable for editor to
              // store data in shared preferences.




              // getting data from gson and storing it in a string.
              val json: String = gson.toJson(courseList)

              // below line is to save data in shared
              // prefs in the form of string.
              editor.putString("courses", json)

              // below line is to apply changes
              // and save data in shared prefs.
              editor.apply()


              notifyDataSetChanged()
          }


      }

        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    fun addList(notificationData : List<String>){
        this.dataList = notificationData
        notifyDataSetChanged()
    }
    fun setOnCheckedClickListener(listener: (String, View) -> Unit) {
        setOnCheckedClickListener = listener
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkedTextView = itemView.findViewById<View>(R.id.option) as CheckBox
    }


}
