package com.example.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ProfileFragment : Fragment() {
    private lateinit var user: TextView
    private lateinit var btn: Button
    private lateinit var sharedPreference: SharedPreferenceLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreference = SharedPreferenceLogin(requireContext())

        val savedEmailLogin = sharedPreference.getValueString("Email")
        view.findViewById<TextView>(R.id.username).text = savedEmailLogin

        btn = view.findViewById(R.id.logout)
        btn.setOnClickListener {
            sharedPreference.removeValue("Email")
            sharedPreference.removeValue("Password")
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        return view
    }
}
