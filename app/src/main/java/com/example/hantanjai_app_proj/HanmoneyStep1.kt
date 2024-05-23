package com.example.hantanjai_app_proj

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hantanjai_app_proj.databinding.ActivityHanmoneyStepOneBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
// เพิ่ม import ด้านล่างนี้
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HanmoneyStep1 : AppCompatActivity() {

    private lateinit var binding: ActivityHanmoneyStepOneBinding
    private lateinit var database: DatabaseReference

    var recyclerView: RecyclerView? = null
    var step1btnconfirm: Button? = null
    var card_view_selectAll: CardView?= null


    //Dialog
    var add_new_friend: Button? = null
    //var test_display: TextView? = null
    //End Dialog


    var friendName = arrayOf("")

    var userProfile = arrayOf<Int>(
        R.drawable.flower01,
        R.drawable.flower02,
        R.drawable.flower03,
        R.drawable.flower04,
        R.drawable.flower05,
        R.drawable.flower06,
        R.drawable.flower01,
        R.drawable.flower02,
        R.drawable.flower03,
        R.drawable.flower04,
        R.drawable.flower05,
        R.drawable.flower06,
        R.drawable.flower07,
        R.drawable.flower05,
        R.drawable.flower06,
        R.drawable.flower01,
        R.drawable.flower02,
        R.drawable.flower03,
        R.drawable.flower04,
        R.drawable.flower05,
        R.drawable.flower06,
        R.drawable.flower07
    )

    //recyclerview
    var myAdapterForHanStepOne = MyAdapterForHanStepOne(friendName, userProfile)

    //Search
    var myList = myAdapterForHanStepOne.getSelectedItems()
    var searchView: SearchView? = null;
    //End Search

    //Dialog
    var newFriendName: String = ""

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hanmoney_step_one)
        init()

        // เปลี่ยน friendName เป็น List
        val friendNameList = mutableListOf<String>()

        // สร้าง listener เพื่อดึงข้อมูลจาก Firebase Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // ลบข้อมูลเก่าทั้งหมด
                friendNameList.clear()

                // วนลูปผ่านข้อมูลทุกตัวที่อยู่ใน "personName"
                for (personSnapshot in snapshot.children) {
                    val personName = personSnapshot.child("personName").getValue(String::class.java)
                    if (!personName.isNullOrEmpty()) {
                        friendNameList.add(personName)
                    }
                }

                // แปลง friendNameList เป็น Array
                friendName = friendNameList.toTypedArray()

                // ตรวจสอบค่า friendName
                for (name in friendName) {
                    Log.d("FriendName", name)
                }

                // อัพเดท RecyclerView
                myAdapterForHanStepOne.updateList(friendNameList.mapIndexed { index, s ->
                    Pair(s, userProfile[index % userProfile.size])
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }
        })


        //val myAdapterForHanStepOne = MyAdapterForHanStepOne(friendName, userProfile)
        recyclerView!!.adapter = myAdapterForHanStepOne

        //add new friend's name
        showEditTextdialog()
        //search view
        searchView()
        //select all
        myAdapterForHanStepOne.selectCardAll(card_view_selectAll)

        step1btnconfirm?.setOnClickListener {
            val selectedIndices = myAdapterForHanStepOne.getSelectedIndices()

            // Filter arrays based on selected indices
            val selectedFriendNames = selectedIndices.map { friendName[it] }.toTypedArray()
            val selectedUserProfiles = selectedIndices.map { userProfile[it] }.toIntArray()

            // Pass the filtered arrays to the next activity
            val intent = Intent(this, Hanmoney_step2::class.java)
            intent.putExtra("selectedFriendNames", selectedFriendNames)
            intent.putExtra("selectedUserProfiles", selectedUserProfiles)

            // Start the next activity
            startActivity(intent)
            finish()
        }
    }

    //Dialog
    private fun showEditTextdialog() {
        add_new_friend?.setOnClickListener {



            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog_layout_hanmoney_step1, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.edt_hs1)

            with(builder) {
                setTitle("เพิ่มรายชื่อ")
                setPositiveButton("ยืนยัน") { dialog, which ->
                    val personName = editText.text.toString() // เลื่อนนี้ไปข้างในบล็อกการคลิก
                    //test_display!!.text = editText.text.toString()
                    if (personName.isEmpty()) {
                        editText.error = "ได้โปรดกรอกข้อมูล"
                    }
                    val personId = database.push().key!!

                    val personn = Person(personId, personName)

                    database.child(personId).setValue(personn)
                }
                setNegativeButton("ยกเลิก") { dialog, which ->
                    Log.d("Main", "Negative button clicked")
                }
                setView(dialogLayout)
                show()
            }

        }
    }
    //End Dialog

    //Search view
    private fun searchView() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }


    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = mutableListOf<Pair<String, Int>>()
            for (i in 0 until friendName.size) {
                if (friendName[i].contains(query, ignoreCase = true)) {
                    filteredList.add(Pair(friendName[i], userProfile[i]))
                }
            }
            // Update RecyclerView with filtered list
            myAdapterForHanStepOne.updateList(filteredList)
        }
    }
    //End Search view

    fun init(){
        recyclerView = findViewById(R.id.recycler_view)
        step1btnconfirm = findViewById(R.id.step1btnconfirm)
        card_view_selectAll = findViewById(R.id.card_view_selectAll)

        //Dialog
        add_new_friend = findViewById(R.id.btntesths1)
        //test_display = findViewById(R.id.hs1_test_dialog)
        //End Dialog

        //Search view
        searchView = findViewById(R.id.searchview)
        //End Search view

        database = FirebaseDatabase.getInstance().getReference("personName")

    }

}