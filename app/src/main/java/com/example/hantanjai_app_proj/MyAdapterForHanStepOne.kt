package com.example.hantanjai_app_proj

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class MyAdapterForHanStepOne(var items:Array<String>, var imageId: Array<Int>):
    RecyclerView.Adapter<MyAdapterForHanStepOne.ViewHolder>(){

    private var selectedItems = mutableListOf<Int>()

    //select all
    private var allItemsSelected = false

    fun selectCardAll(cardViewSelectAll: CardView?) {
        cardViewSelectAll?.setOnClickListener {
            if (!allItemsSelected) {
                // Select all items in the RecyclerView
                selectAllItems()
                allItemsSelected = true
            } else {
                // Deselect all items in the RecyclerView
                deselectAllItems()
                allItemsSelected = false
            }

            // Notify the RecyclerView to update the UI
            notifyDataSetChanged()
        }
    }

    fun selectAllItems() {
        selectedItems.clear()
        selectedItems.addAll(0 until itemCount)
        allItemsSelected = true
        notifyDataSetChanged()
    }

    fun deselectAllItems() {
        selectedItems.clear()
        allItemsSelected = false
        notifyDataSetChanged()
    }
    //end select all

    //send
    // Function to get selected indices
    fun getSelectedIndices(): List<Int> {
        return selectedItems.toList()
    }
    //end send

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_task,
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = selectedItems.contains(position)

        holder.nameTextView?.text = items[position]
        holder.imageIdView?.setImageResource(imageId[position])


        // Change background color based on selection
        val backgroundColor =
            if (isSelected || allItemsSelected) R.color.ColorLightGray else R.color.white

        // Use setBackgroundColor to set the background color
        holder.cardView?.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                backgroundColor
            )
        )

        // Set the click listener for item selection
        holder.itemView.setOnClickListener {
            if (isSelected) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            allItemsSelected = false
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Function to get selected items
    fun getSelectedItems(): List<Pair<String, Int>> {
        return selectedItems.map { position ->
            Pair(items[position], imageId[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var nameTextView : TextView? = itemView.findViewById(R.id.task_title_select_all)
        internal var imageIdView : CircleImageView? = itemView.findViewById(R.id.profile_image_step1)
        internal var cardView: CardView? = itemView.findViewById(R.id.card_view_selectAll)
    }

    //search view
    fun updateList(newList: List<Pair<String, Int>>) {
        items = newList.map { it.first }.toTypedArray()
        imageId = newList.map { it.second }.toTypedArray()
        notifyDataSetChanged()
    }
    //End search view
}