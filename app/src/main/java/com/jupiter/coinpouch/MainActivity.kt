package com.jupiter.coinpouch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var user:MutableMap<String, Any> = HashMap()
    var user2:MutableMap<String, Any> = HashMap()
    var user3:MutableMap<String, Any> = HashMap()
    var user4:MutableMap<String, Any> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this)
            .load(R.drawable.coinpouch_icon)
            .override(200, 200)
            .into(imgView)

        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val c = Calendar.getInstance()
        val dts = sdf.format(c.getTime())

        btnLogin.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                db.collection(txtName.text.toString())
                        .whereEqualTo("username",txtName.text.toString())
                        .get()
                        .addOnSuccessListener{result->
                            for(document in result)
                                if(document.data["username"] == txtName.text.toString() && document.data["password"] == txtPassword.text.toString()){
                                   intent = Intent(this@MainActivity,MainActivity2::class.java).apply{
                                        putExtra("username",txtName.text.toString())
                                    }
                                    startActivity(intent)
                                }
                                else if(document.data["username"] != txtName.text.toString()) {
                                    txtError.text = "Fail"
                                }

                        }
                        }


        })

        btnRegister.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                user["username"] = txtName.text.toString()
                user["password"] = txtPassword.text.toString()
                user2["out"] = 0
                user3["in"] = 0
                user4["budget"] = 0
                db.collection(txtName.text.toString())
                        .document("account")
                        .set(user)

                db.collection(txtName.text.toString())
                        .document(dts.toString())
                        .set(user4)

                db.collection(txtName.text.toString())
                    .document(dts.toString())
                    .collection("input")
                    .document("default")
                    .set(user3)

                db.collection(txtName.text.toString())
                    .document(dts.toString())
                    .collection("output")
                    .document("default")
                    .set(user2)

                intent = Intent(this@MainActivity,MainActivity2::class.java).apply{
                    putExtra("username",txtName.text.toString())
                }
                startActivity(intent)
            }
        })
    }
}