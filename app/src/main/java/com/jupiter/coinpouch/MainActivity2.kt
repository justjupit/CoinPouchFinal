package com.jupiter.coinpouch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity2 : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var user:MutableMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var intent = getIntent()
        val name: String? = intent.getStringExtra("username")
        txtUser.text = name.toString()

        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val c = Calendar.getInstance()
        val dts = sdf.format(c.getTime())
        txtDate.text = dts.toString()

        reportOut()
        reportIn()
        reportBudget()


        prvDay.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                c.add(Calendar.DATE,-1)
                val dtn = sdf.format(c.getTime())
                txtDate.text = dtn.toString()
                reportOut()
                reportIn()
                reportBudget()
            }
        })
        nxtDay.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                c.add(Calendar.DATE,1)
                val dtn = sdf.format(c.getTime())
                txtDate.text = dtn.toString()
                reportOut()
                reportIn()
                reportBudget()
            }
        })
        btnTransaction.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                intent = Intent(this@MainActivity2,Menu::class.java).apply{
                    putExtra("date",txtDate.text.toString())
                    putExtra("name_menu",txtUser.text.toString())
                }
                startActivityForResult(intent,99)
            }
        })

        btnBudget.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                intent = Intent(this@MainActivity2,Budget::class.java).apply {
                    putExtra("name",txtUser.text.toString())
                    putExtra("date",txtDate.text.toString())
                }
                startActivityForResult(intent,99)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==99){
            intent = Intent(this@MainActivity2,MainActivity2::class.java).apply{
            }
            startActivity(intent)
            finish()
        }
    }

    fun reportOut(){
        db.collection(txtUser.text.toString())
                .document(txtDate.text.toString())
                .collection("output")
                .get()
                .addOnSuccessListener{ result ->
                    var msg:String =""

                    for(document in result){
                        reportOut.setTextColor(Color.RED)

                        msg+=document.id+"\n"+"- "+document.data["out"].toString()+ "\n\n"

                    }
                    if (msg!=""){
                        reportOut.text = msg

                    }
                    else reportOut.text = ""
                }
            .addOnFailureListener{

            }

        db.collection(txtUser.text.toString())
            .document(txtDate.text.toString())
            .collection("output")
            .get()
            .addOnSuccessListener {
                var totalout:Int = 0
                for (document in it){
                    val out = document.data["out"].toString().toInt()
                    totalout+=out
                }
                txtTotalOut.setTextColor(Color.RED)
                txtTotalOut.text ="Total Output= "+totalout.toString()
            }
            .addOnFailureListener{

            }

    }

    fun reportIn(){
        db.collection(txtUser.text.toString())
                .document(txtDate.text.toString())
                .collection("input")
                .get()
                .addOnSuccessListener{ result ->
                    var msg:String =""
                    for(document in result){
                        reportIn.setTextColor(Color.GREEN)
                        msg+=document.id+"\n"+"+ "+document.data["in"].toString()+ "\n\n"
                    }
                    if (msg!="") reportIn.text = msg
                    else reportIn.text = ""
                }
                .addOnFailureListener{
                    Toast.makeText(baseContext,"No Data",Toast.LENGTH_LONG).show()
                 }

        db.collection(txtUser.text.toString())
            .document(txtDate.text.toString())
            .collection("input")
            .get()
            .addOnSuccessListener {
                var totalinp:Int = 0
                for (document in it){
                    val inp = document.data["in"].toString().toInt()
                    totalinp+=inp
                }
                txtTotalIn.setTextColor(Color.GREEN)
                txtTotalIn.text ="Total Input= "+totalinp.toString()
            }
            .addOnFailureListener{
                Toast.makeText(baseContext,"No Data",Toast.LENGTH_LONG).show()
            }
    }

    fun reportBudget(){
        db.collection(txtUser.text.toString())
            .document(txtDate.text.toString())
            .get()
            .addOnSuccessListener(){
                var bug:Int = it.data?.get("budget").toString().toInt()
                var msgbudget:String =""
               msgbudget +="The Day's Budget = "+it.data?.get("budget").toString()
                if(msgbudget!="") txtTodayBudget.text =  msgbudget
                else txtTodayBudget.text = "Budget hasn't been set"
            }
            .addOnFailureListener{
                Toast.makeText(baseContext,"No Data",Toast.LENGTH_LONG).show()
            }
    }
}

