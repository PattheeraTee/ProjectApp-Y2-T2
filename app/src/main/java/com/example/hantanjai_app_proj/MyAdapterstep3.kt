package com.example.hantanjai_app_proj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapterstep3(
    private val friendNames: List<String>,
    private val imageIds: List<Int>,
    private val editTextValues: MutableList<String?>
) : RecyclerView.Adapter<MyAdapterstep3.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_task_pay, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = friendNames[position]
        holder.imageIdView.setImageResource(imageIds[position])

        // Assume there is a TextView in your_item_layout_for_step3 to display the editText value
        holder.editTextView.text = editTextValues[position] ?: ""
    }

    override fun getItemCount(): Int {
        return friendNames.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var nameTextView: TextView = itemView.findViewById(R.id.task_titlestep3)
        internal var imageIdView: CircleImageView = itemView.findViewById(R.id.profile_imagestep3)
        internal var editTextView: TextView = itemView.findViewById(R.id.memberpay_step3)
    }
}
