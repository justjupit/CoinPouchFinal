package com.jupiter.coinpouch

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_budget.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.txtUser
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.txtDate
import java.util.HashMap

class Budget : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var user:MutableMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)

        var intent = getIntent()
        val name:String? = intent.getStringExtra("name")
        val date:String? = intent.getStringExtra("date")
        txtUserBudget.text = name.toString()
        dateBudget.text = date.toString()
        
        btnUpdateBudget.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                user["budget"]= txtBudget.text.toString().toInt()
                db.collection(txtUserBudget.text.toString())
                    .document(dateBudget.text.toString())
                    .set(user)
                intent = Intent(this@Budget,MainActivity2::class.java).apply {
                    putExtra("username",txtUserBudget.text.toString())
                }
                startActivity(intent)
            }
        })

    }
}