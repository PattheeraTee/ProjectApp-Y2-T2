package com.example.hantanjai_app_proj
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.hantanjai_app_proj.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try {
            val totalValue = intent.getDoubleExtra("totalValue", 0.0)
            val editTextValues = intent.getStringArrayExtra("editTextValues") ?: emptyArray()
            val namesAndValuesRecycle1 = intent.getStringExtra("namesAndValuesRecycle1")
            val namesAndValuesRecycle2 = intent.getStringExtra("namesAndValuesRecycle2")

            val initialPayments = calculatePayments(namesAndValuesRecycle1)
            val actualPayments = calculatePayments(namesAndValuesRecycle2)
//            val differences = initialPayments.keys.associateWith {
//                initialPayments[it]!! - actualPayments.getOrDefault(
//                    it,
//                    0
//                )
//            }.toMutableMap()
            val differences = initialPayments.keys.associateWith {
                initialPayments[it]!! - actualPayments.getOrDefault(it, 0.0)
            }.toMutableMap()
            val textview3 = findViewById<TextView>(R.id.maintextthree)

            // Log differences for debugging
            Log.d("MainActivity", "Differences: $differences")
            logAndDisplay("Differences: $differences", textview3)

            // Adjust payments until no more adjustments can be made

            var changesMade: Boolean
            do {
                changesMade = adjustPayments(differences)
            } while (changesMade)

            // Final differences
            val textview4 = findViewById<TextView>(R.id.maintextfour)

            Log.d("MainActivity", "Final differences: $differences")
            logAndDisplay("Final differences: $differences", textview4)

            // Use the received values as needed
            val totalTextView = findViewById<TextView>(R.id.maintextotal)
            totalTextView.text = "Total: $totalValue à¸¿"

            val recycle1TextView = findViewById<TextView>(R.id.maintextone)
            recycle1TextView.text = "Recycle 1: $namesAndValuesRecycle1"

            val recycle2TextView = findViewById<TextView>(R.id.maintexttwo)
            recycle2TextView.text = "Recycle 2: $namesAndValuesRecycle2"



            // Set up ActionBar
            supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            supportActionBar?.setCustomView(R.layout.action_bar_layout)
            val actionBarTextView: AppCompatTextView =
                supportActionBar?.customView?.findViewById(R.id.action_bar)
                    ?: throw IllegalStateException("action_bar TextView not found in custom ActionBar layout")

            // Update the text of the TextView
            actionBarTextView.text = "New ActionBar Title"

            replaceFragment(Home())

            binding.bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> replaceFragment(Home())
                    R.id.notification -> replaceFragment(Notification())
                    R.id.other -> replaceFragment(Other())
                    R.id.pay_bill -> replaceFragment(Paybill())
                    else -> {
                        // Handle other menu items if needed
                    }
                }
                true
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
            showToast("An error occurred. Please check logs.")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.commit()
    }

    private fun calculatePayments(namesAndValues: String?): Map<String, Double> {
        val payments = mutableMapOf<String, Double>()
        namesAndValues?.split(", ")?.forEach {
            val (name, value) = it.split(": ")
            payments[name] = value.toDouble()
        }
        return payments
    }



//    private fun adjustPayments(differences: MutableMap<String, Double>): Boolean {
//        var changesMade = false
//
//        for (payer in differences.keys.toList()) {
//            for (payee in differences.keys.toList()) {
//                if (payer != payee) {
//                    // Determine the amount that can be adjusted
//                    val payerAmount = differences.getValue(payer)
//                    val payeeAmount = differences.getValue(payee)
//                    val amount = minOf(-payerAmount, payeeAmount)
//
//                    if (amount > 0) {
//                        val message = "$payer pays $payee an additional $amount"
//                        val textview5 = findViewById<TextView>(R.id.maintextfive)
//
//                        logAndDisplay(message, textview5)
//
//                        showToast(message)
//                        Log.d("MainActivity", "$payer pays $payee an additional $amount")
//                        differences[payer] = payerAmount + amount
//                        differences[payee] = payeeAmount - amount
//                        changesMade = true
//                    }
//                }
//            }
//        }
//
//        return changesMade
//    }

    private fun adjustPayments(differences: MutableMap<String, Double>): Boolean {
        var changesMade = false
        val textview5 = findViewById<TextView>(R.id.maintextfive)


        for (payer in differences.keys.toList()) {
            for (payee in differences.keys.toList()) {
                if (payer != payee) {
                    // Determine the amount that can be adjusted
                    val payerAmount = differences.getValue(payer)
                    val payeeAmount = differences.getValue(payee)
                    val amount = minOf(-payerAmount, payeeAmount)

                    if (amount > 0) {
                        val message = "$payer ต้องจ่ายให้ $payee จำนวนเงิน $amount บาท"

                        // Append the message to textview5
                        textview5.append(message + "\n")

                        showToast(message)
                        Log.d("MainActivity", message)
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logAndDisplay(message: String, textView: TextView) {
        Log.d("MainActivity", message)
        textView.text = message
    }


}
