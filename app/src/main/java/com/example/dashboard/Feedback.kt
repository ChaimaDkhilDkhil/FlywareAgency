package com.example.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class Feedback : AppCompatActivity() {
    private lateinit var subject : EditText
    private lateinit var message : EditText
    private lateinit var btn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        subject=findViewById(R.id.name)
        message=findViewById(R.id.feedbtn)
        btn=findViewById(R.id.sendBtn)
        btn.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO) {
                val response = try {
                    Log.e("feedback",message.text.toString())
                    RetrofitInstance.api.sendEmail(
                        MailForm(subject.text.toString(),message.text.toString())
                    )
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "HTTP error ${e.code()}: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "App error ${e.message}", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            btn,
                            "email send successfully",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }


    }
}