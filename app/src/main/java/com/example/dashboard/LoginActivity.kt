package com.example.dashboard

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


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
        val logInLayout = findViewById<LinearLayout>(R.id.logInLayout)
        val signIn = findViewById<TextView>(R.id.singIn)
        sharedPreference = SharedPreferenceLogin(this)
        sharedPreferenceSignup = SharedPreferenceSignup(this)
        emailLogin = findViewById(R.id.eMailLogin)
        passwordLogin = findViewById(R.id.passwordLogin)
        checkBox = findViewById(R.id.checkBox)
        email = findViewById(R.id.eMail)
        password = findViewById(R.id.password)
        confirmedPassword = findViewById(R.id.confirmPassword)
        signup = findViewById(R.id.signup)
        val savedEmailLogin = sharedPreference.getValueString("EmailLogin")
        val savedPasswordLogin = sharedPreference.getValueString("PasswordLogin")


        if (savedEmailLogin != "" || savedPasswordLogin != "") {
            emailLogin.setText(savedEmailLogin)
            passwordLogin.setText(savedPasswordLogin)
        }

        singUp.setOnClickListener {
            singUp.background = resources.getDrawable(R.drawable.switch_trcks, null)
            singUp.setTextColor(resources.getColor(R.color.textColor, null))
            logIn.background = null
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            logIn.setTextColor(resources.getColor(R.color.teal_700, null))
        }

        logIn.setOnClickListener {
            singUp.background = null
            singUp.setTextColor(resources.getColor(R.color.teal_700))
            logIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            logIn.setTextColor(resources.getColor(R.color.textColor))
        }
        signIn.setOnClickListener {
            val name = emailLogin.text.toString()
            val password = passwordLogin.text.toString()

            if (name != "" && password != "" && checkBox.isChecked) {
                sharedPreference.save("EmailLogin", name)
                sharedPreference.save("PasswordLogin", password)
            } else {
                sharedPreference.removeValue("EmailLogin")
                sharedPreference.removeValue("PasswordLogin")
            }
            if ((emailLogin.getText().length === 0) || (passwordLogin.getText().length === 0)) {
                val ad: AlertDialog.Builder
                ad = AlertDialog.Builder(this)
                ad.setMessage("Les champs ne doivent pas être vide")
                ad.setTitle("Error")
                ad.setIcon(android.R.drawable.btn_dialog)
                val a = ad.create()
                a.show()
            } else {
                val user = User(null, name, password)

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = RetrofitInstance.api.signin(user)

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful && response.body() != null) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                response.body()!!._id?.let { it1 ->
                                    sharedPreference.save(
                                        "id",
                                        it1
                                    )
                                }
                                sharedPreference.save("Email", name)
                                sharedPreference.save("Password", password)
                            } else {
                                val ad: AlertDialog.Builder =
                                    AlertDialog.Builder(this@LoginActivity)
                                ad.setMessage("email ou password incorrecte")
                                ad.setTitle("Error")
                                ad.setIcon(android.R.drawable.btn_dialog)
                                val a = ad.create()
                                a.show()
                            }
                        }
                    } catch (e: HttpException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "HTTP error ${e.code()}: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "App error ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        e.printStackTrace()
                    }
                }
            }
        }
        signup.setOnClickListener {
            val name = email.text.toString()
            val password = password.text.toString()
            val confirmedPassword = confirmedPassword.text.toString()

            if (!password.equals(confirmedPassword)) {
                val ad: AlertDialog.Builder = AlertDialog.Builder(this)
                ad.setMessage("Le mot de passe et la confirmation du mot de passe doivent être identiques")
                ad.setTitle("Erreur")
                ad.setIcon(android.R.drawable.btn_dialog)
                val a: AlertDialog = ad.create()
                a.show()
            } else if (name.isNotEmpty() && password.isNotEmpty() && confirmedPassword.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        // Effectuer la demande d'inscription
                        val response = RetrofitInstance.api.signup(User(null, name, password))

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Compte créé avec succès",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val responseBody = response.errorBody()?.string()
                                if (responseBody?.contains("Username already exists") == true) {
                                    val ad: AlertDialog.Builder =
                                        AlertDialog.Builder(this@LoginActivity)
                                    ad.setMessage("Le nom d'utilisateur existe déjà. Veuillez en choisir un autre.")
                                    ad.setTitle("Erreur")
                                    ad.setIcon(android.R.drawable.btn_dialog)
                                    val a: AlertDialog = ad.create()
                                    a.show()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Erreur inconnue lors de l'inscription",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } catch (e: IOException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                applicationContext,
                                "Erreur de l'application ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        e.printStackTrace()
                    }
                }
            } else {
                val ad: AlertDialog.Builder = AlertDialog.Builder(this)
                ad.setMessage("Les champs ne doivent pas être vides")
                ad.setTitle("Erreur")
                ad.setIcon(android.R.drawable.btn_dialog)
                val a: AlertDialog = ad.create()
                a.show()
            }
        }

    }
    }
