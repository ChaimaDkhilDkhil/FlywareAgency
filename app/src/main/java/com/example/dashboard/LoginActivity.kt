package com.example.dashboard

import android.annotation.SuppressLint
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog


class LoginActivity : AppCompatActivity() {
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmedPassword: EditText
    private lateinit var signup: Button
    private lateinit var sharedPreference: SharedPreferenceLogin
    private lateinit var sharedPreferenceSignup: SharedPreferenceSignup

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val singUp = findViewById<TextView>(R.id.singUp)
        val logIn = findViewById<TextView>(R.id.logIn)
        val singUpLayout = findViewById<LinearLayout>(R.id.singUpLayout)
        val logInLayout  = findViewById<LinearLayout>(R.id.logInLayout)
        val signIn = findViewById<TextView>(R.id.singIn)
        sharedPreference = SharedPreferenceLogin(this)
        sharedPreferenceSignup = SharedPreferenceSignup(this)
        emailLogin=findViewById(R.id.eMailLogin)
        passwordLogin=findViewById(R.id.passwordLogin)
        checkBox=findViewById(R.id.checkBox)
        email=findViewById(R.id.eMail)
        password=findViewById(R.id.password)
        confirmedPassword=findViewById(R.id.confirmPassword)
        signup=findViewById(R.id.signup)
        val savedEmailLogin = sharedPreference.getValueString("EmailLogin")
        val savedPasswordLogin = sharedPreference.getValueString("PasswordLogin")
        val savedEmail = sharedPreferenceSignup.getValueString("Email")
        val savedPassword = sharedPreferenceSignup.getValueString("Password")

        if (savedEmailLogin !="" || savedPasswordLogin !=""){
            emailLogin.setText(savedEmailLogin)
            passwordLogin.setText(savedPasswordLogin)
        }

        singUp.setOnClickListener {
            singUp.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUp.setTextColor(resources.getColor(R.color.textColor,null))
            logIn.background = null
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            logIn.setTextColor(resources.getColor(R.color.teal_700,null))
        }

        logIn.setOnClickListener {
            singUp.background = null
            singUp.setTextColor(resources.getColor(R.color.teal_700))
            logIn.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            logIn.setTextColor(resources.getColor(R.color.textColor))
        }
        signIn.setOnClickListener {
            val name = emailLogin.text.toString()
            val password = passwordLogin.text.toString()

            if(name !="" && password !="" && checkBox.isChecked){
                sharedPreference.save("EmailLogin", name)
                sharedPreference.save("PasswordLogin", password)
            }else{
                sharedPreference.clearSharedPreference()
            }
            if ((emailLogin.getText().length===0)||(passwordLogin.getText().length===0)){
                val ad: AlertDialog.Builder
                ad = AlertDialog.Builder(this)
                ad.setMessage("Les champs ne doivent pas être vide")
                ad.setTitle("Error")
                ad.setIcon(android.R.drawable.btn_dialog)
                val a = ad.create()
                a.show()
            }
            else{
                if ((name.equals(savedEmail))&&(password.equals(savedPassword))){
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))}
                else{
                    val ad: AlertDialog.Builder
                    ad = AlertDialog.Builder(this)
                    ad.setMessage("email ou password incorrecte")
                    ad.setTitle("Error")
                    ad.setIcon(android.R.drawable.btn_dialog)
                    val a = ad.create()
                    a.show()}
            }
        }
        signup.setOnClickListener {
            val name = email.text.toString()
            val password = password.text.toString()
            val confirmedPassword = confirmedPassword.text.toString()
            if (!password.equals(confirmedPassword)){
                val ad: AlertDialog.Builder
                ad = AlertDialog.Builder(this)
                ad.setMessage("password et confirmed password doivent être égaux")
                ad.setTitle("Error")
                ad.setIcon(android.R.drawable.btn_dialog)
                val a = ad.create()
                a.show()}
            else if(name !="" && password !="" && confirmedPassword !=""){
                sharedPreferenceSignup.save("Email", name)
                sharedPreferenceSignup.save("Password", password)
                Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_LONG).show();
            }else{
                val ad: AlertDialog.Builder
                ad = AlertDialog.Builder(this)
                ad.setMessage("Les champs ne doivent pas être vide")
                ad.setTitle("Error")
                ad.setIcon(android.R.drawable.btn_dialog)
                val a = ad.create()
                a.show()
            }
        }
    }

}
