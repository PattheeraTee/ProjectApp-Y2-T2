package com.example.hantanjai_app_proj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar


class Hanmoney_step2 : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var recyclerView2: RecyclerView? = null
    var navbarback: ImageView? = null
    var navbaredit: EditText? = null
    var btnconfirm: Button? = null
    var radiohanequl: RadioButton? = null
    var radiohanunequl: RadioButton? = null
    var showtotal: TextView? = null
    private var editTextValues: MutableList<String?> = mutableListOf()
    var selectedFriendNames: Array<String>? = null
    var selectedUserProfiles: IntArray? = null
    var btnback : Button? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hanmoney_step2)
        init()
        getSupportActionBar()?.hide()
        navbarback?.setOnClickListener {
            val intent = Intent(this, HanmoneyStep1::class.java)
            startActivity(intent)
            finish()
        }
        btnback?.setOnClickListener {
            val intent = Intent(this, HanmoneyStep1::class.java)
            startActivity(intent)
            finish()
        }
        selectedFriendNames = intent.getStringArrayExtra("selectedFriendNames")
        selectedUserProfiles = intent.getIntArrayExtra("selectedUserProfiles")

        if (selectedFriendNames != null && selectedUserProfiles != null) {
            val calendar = Calendar.getInstance()
            val currentDate = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
            val textViewDate = findViewById<TextView>(R.id.step2datecurent)
            textViewDate.text = currentDate
            val myAdapter = MyAdapterstep2(
                selectedFriendNames!!.toList(),
                selectedUserProfiles!!.toList(),
                editTextValues
            ) { updatedValues ->
                editTextValues = updatedValues.toMutableList()
            }
            recyclerView!!.adapter = myAdapter
            radiohanequl!!.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Calculate total based on editTextValues
                    val total = editTextValues
                        .filterNotNull()
                        .map { it.toDoubleOrNull() ?: 0.0 }
                        .sum()
                    // Calculate the equal amount for each person
                    val equalAmount = total / selectedFriendNames!!.size
                    // Update editTextValues with equal amount for each person
                    editTextValues = MutableList(selectedFriendNames!!.size) { equalAmount.toString() }
                    // Display the total amount in showtotal TextView
                    showtotal?.text = "$total ฿"
                }
            }
            radiohanunequl!!.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Show recyclerView2 when radiohanunequl is checked
                    recyclerView2?.visibility = View.VISIBLE
                    // Pass editTextValues to MyAdapterstep2 for recyclerView2
                    val myAdapter2 = MyAdapterstep2(
                        selectedFriendNames!!.toList(),
                        selectedUserProfiles!!.toList(),
                        editTextValues
                    ) { updatedValues ->
                        editTextValues = updatedValues.toMutableList()
                    }
                    recyclerView2!!.adapter = myAdapter2
                    val total = editTextValues
                        .filterNotNull()
                        .map { it.toDoubleOrNull() ?: 0.0 }
                        .sum()
                    showtotal?.text = "$total ฿"
                }else {
                    // Hide recyclerView2 when radiohanunequl is not checked
                    recyclerView2?.visibility = View.GONE
                }
            }
            btnconfirm!!.setOnClickListener {
                // Calculate total based on editTextValues and display in showtotal TextView
                val total = editTextValues
                    .filterNotNull()
                    .map { it.toDoubleOrNull() ?: 0.0 }
                    .sum()

                // Display a Toast message with the total value and names of each person in RecyclerView อันแรก
                val namesAndValuesRecycle1 = selectedFriendNames!!.zip(myAdapter.editTextValues)
                    .map { "${it.first}: ${it.second}" }
                    .joinToString(", ")

                // Display a Toast message with the names and values of each person in RecyclerView อันที่สอง
                val namesAndValuesRecycle2 = editTextValues.mapIndexed { index, value ->
                    val personName = selectedFriendNames!![index % selectedFriendNames!!.size]
                    if (value != null) {
                        "$personName: $value"
                    } else {
                        "$personName: N/A"
                    }
                }.joinToString(", ")

                // Display both Toast messages
                Toast.makeText(
                    this,
                    "Total: $total ฿\nMembers Recycle 1: $namesAndValuesRecycle1\nMembers Recycle 2: $namesAndValuesRecycle2",
                    Toast.LENGTH_LONG
                ).show()

                // Create an Intent to start the HomeActivity
                val intent = Intent(this,Hanmoney_step3::class.java)

                // Pass the necessary data to the next activity
                intent.putExtra("totalValue", total)
                intent.putExtra("editTextValues", editTextValues.toTypedArray())
                intent.putExtra("namesAndValuesRecycle1", namesAndValuesRecycle1)
                intent.putExtra("namesAndValuesRecycle2", namesAndValuesRecycle2)
                intent.putExtra("navbareditValue", navbaredit?.text.toString())
                intent.putExtra("textViewDateValue", textViewDate.text.toString())
                intent.putExtra("textViewDateValue", textViewDate.text.toString())
                intent.putExtra("imageIds", selectedUserProfiles)

                // Start the next activity
                startActivity(intent)
                finish() // Finish the current activity
            }

        } else {
            // Handle the case where intent extras are null
            Toast.makeText(this, "Error: Intent extras are null", Toast.LENGTH_LONG).show()
            finish() // Finish the activity or handle the error accordingly
        }


    }
    fun init(){
        recyclerView = findViewById(R.id.recycleviewstep2)
        recyclerView2 = findViewById(R.id.hanrecycleviewstep2)
        navbarback = findViewById(R.id.navbarstep2back)
        navbaredit = findViewById(R.id.navbarstep2edt)
        btnconfirm = findViewById(R.id.step2btnconfirm)
        radiohanequl = findViewById(R.id.hanequl_layout)
        radiohanunequl = findViewById(R.id.hanunequl_layout)
        showtotal = findViewById(R.id.step2calall)
        btnback = findViewById(R.id.step2btnback)
    }

}
