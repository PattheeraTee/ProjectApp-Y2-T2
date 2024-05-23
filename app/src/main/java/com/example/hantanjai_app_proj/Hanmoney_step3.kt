//package com.example.hantanjai_app_proj
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//
//class Hanmoney_step3 : AppCompatActivity() {
//    //
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_hanmoney_step3)
//    }
//
//}
package com.example.hantanjai_app_proj

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class Hanmoney_step3 : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var recyclerView2: RecyclerView? = null
    var showpeoplepay: TextView? = null
    var billname: TextView? = null
    var billtotal: TextView? = null
    var dbilltotal: TextView? = null
    var datecurrent: TextView? = null
    private var payer: String? = null
    private var payee: String? = null
    var btncomfirm : Button? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hanmoney_stepthree)
        init()
        supportActionBar?.hide()
        btncomfirm?.setOnClickListener {
            val intent = Intent(this, HanmoneyStep1::class.java)
            startActivity(intent)
            finish()
        }
        val totalValue = intent.getDoubleExtra("totalValue", 0.0)
        val editTextValues = intent.getStringArrayExtra("editTextValues") ?: emptyArray()
        val namesAndValuesRecycle1 = intent.getStringExtra("namesAndValuesRecycle1")
        val namesAndValuesRecycle2 = intent.getStringExtra("namesAndValuesRecycle2")
        val initialPayments = calculatePayments(namesAndValuesRecycle1)
        val actualPayments = calculatePayments(namesAndValuesRecycle2)
        val navbareditValue = intent.getStringExtra("navbareditValue")
        val textViewDateValue = intent.getStringExtra("textViewDateValue")
        val imageIds = intent.getIntArrayExtra("imageIds") ?: intArrayOf()
        val differences = initialPayments.keys.associateWith {
            initialPayments[it]!! - actualPayments.getOrDefault(it, 0.0)
        }.toMutableMap()

        val recyclerViewData1 = createRecyclerViewData(namesAndValuesRecycle1)
        val recyclerViewData2 = createRecyclerViewData(namesAndValuesRecycle2)
        val valuesFromRecycle1 = namesAndValuesRecycle1?.split(", ")?.map {
            it.split(": ")[1].toDouble().toString() // Convert to String
        } ?: emptyList()

        val myAdapter1 = MyAdapterstep3(
            recyclerViewData1.map { it.name },
            imageIds.toList(),
            valuesFromRecycle1.toMutableList()
        )



        val myAdapter2 = MyAdapterstep3(
            recyclerViewData2.map { it.name },
            imageIds.toList(),
            editTextValues.toMutableList()
        )

        recyclerView?.adapter = myAdapter1
        recyclerView2?.adapter = myAdapter2

        dbilltotal?.text = "$totalValue ฿"
        billtotal?.text = "$totalValue ฿"
        billname?.text = "$navbareditValue"
        datecurrent?.text = "$textViewDateValue"

        // Adjust payments until no more adjustments can be made
        var changesMade: Boolean
        do {
            changesMade = adjustPayments(differences)
        } while (changesMade)
    }

    private fun createRecyclerViewData(namesAndValues: String?): List<RecyclerViewItem> {
        val recyclerViewData = mutableListOf<RecyclerViewItem>()

        namesAndValues?.split(", ")?.forEach {
            val (name, value) = it.split(": ")
            recyclerViewData.add(RecyclerViewItem(name, value.toDouble(), 0))
        }

        return recyclerViewData
    }


    fun calculatePayments(namesAndValues: String?): Map<String, Double> {
        val payments = mutableMapOf<String, Double>()
        namesAndValues?.split(", ")?.forEach {
            val (name, value) = it.split(": ")
            payments[name] = value.toDouble()
        }
        return payments
    }

    private fun adjustPayments(differences: MutableMap<String, Double>): Boolean {
        var changesMade = false
        for (payer in differences.keys.toList()) {
            for (payee in differences.keys.toList()) {
                if (payer != payee) {
                    // Store payer and payee values
                    this.payer = payer
                    this.payee = payee

                    // Determine the amount that can be adjusted
                    val payerAmount = differences.getValue(payer)
                    val payeeAmount = differences.getValue(payee)
                    val amount = minOf(-payerAmount, payeeAmount)

                    if (amount > 0) {
                        val message = "$payer ต้องจ่ายให้ $payee                                   จำนวนเงิน $amount บาท"

                        showpeoplepay?.append(
                            Html.fromHtml(
                                "<html><body><div style='display: flex; justify-content: space-between;'>" +
                                        "<span style='color: black;'>$payer ต้องจ่ายให้ $payee </span>" +
                                        "<span style='color: red;'>จำนวนเงิน $amount บาท</span></div></body></html>",
                                Html.FROM_HTML_MODE_LEGACY
                            ) as Spanned
                        )

                        // Add a new line
//                        showpeoplepay?.append("\n")
                        // Add a new line
//                        showToast(message)

                        differences[payer] = payerAmount + amount
                        differences[payee] = payeeAmount - amount
                        changesMade = true
                    }
                }
            }
        }
        return changesMade
    }

    private fun showToast(message: String) {
        val coloredMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message.replace("$payer", "<font color='red'>$payer</font>")
                .replace("$payee", "<font color='red'>$payee</font>"), Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(message.replace("$payer", "<font color='red'>$payer</font>")
                .replace("$payee", "<font color='red'>$payee</font>"))
        }

        Toast.makeText(this, coloredMessage, Toast.LENGTH_SHORT).show()
    }

    private fun init() {
        recyclerView = findViewById(R.id.recycleviewstep3)
        recyclerView2 = findViewById(R.id.hanrecycleviewstep3)
        showpeoplepay = findViewById(R.id.textshowhan)
        billname = findViewById(R.id.billname3)
        billtotal = findViewById(R.id.billtotal3)
        dbilltotal = findViewById(R.id.step3calall)
        datecurrent = findViewById(R.id.step3datecurentt)
        btncomfirm = findViewById(R.id.step3btnconfirmmmmmm)
    }
}

// Declare RecyclerViewItem outside the Hanmoney_step3 class
data class RecyclerViewItem(val name: String, val amount: Double, val imageId: Int)