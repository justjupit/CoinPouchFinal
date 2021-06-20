package com.jupiter.coinpouch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.content_main.*

class Menu : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var user:MutableMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        var intent = getIntent()
        val date:String? = intent.getStringExtra("date")
        val name:String? = intent.getStringExtra("name_menu")

        dateTransaction.text = date.toString()
        txtUserMenu.text = name.toString()

        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(applicationContext," On checked change :"+
                        " ${radio.text}",
                    Toast.LENGTH_SHORT).show()
            })


        updateOutput.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {

                if (radbtnOutput.isChecked()){


                    user["out"]= txtAmountOutput.text.toString().toInt()
                    db.collection(txtUserMenu.text.toString())
                            .document(dateTransaction.text.toString())
                            .collection("output")
                            .document(txtReasonOutput.text.toString())
                            .set(user)
                    intent = Intent(this@Menu,MainActivity2::class.java).apply {
                        putExtra("username",txtUserMenu.text.toString())
                    }
                    startActivity(intent)

                }
                else if(radbtnInput.isChecked()){


                    user["in"]= txtAmountOutput.text.toString().toInt()
                    db.collection(txtUserMenu.text.toString())
                            .document(dateTransaction.text.toString())
                            .collection("input")
                            .document(txtReasonOutput.text.toString())
                            .set(user)
                    intent = Intent(this@Menu,MainActivity2::class.java).apply {
                        putExtra("username",txtUserMenu.text.toString())
                    }
                    startActivity(intent)
                }



            }
        })
    }
}