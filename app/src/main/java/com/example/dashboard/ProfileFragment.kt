import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.dashboard.LoginActivity
import com.example.dashboard.R
import com.example.dashboard.SharedPreferenceLogin

class ProfileFragment : Fragment() {
    private lateinit var user: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnFeedback: Button
    private lateinit var sharedPreference: SharedPreferenceLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreference = SharedPreferenceLogin(requireContext())

        val savedEmailLogin = sharedPreference.getValueString("Email")
        view.findViewById<TextView>(R.id.username).text = savedEmailLogin

        // Logout Button
        btnLogout = view.findViewById(R.id.logout)
        btnLogout.setOnClickListener {
            handleLogout()
        }

        // Feedback Button
        btnFeedback = view.findViewById(R.id.feedbutton)
        btnFeedback.setOnClickListener {
            handleFeedback()
        }

        return view
    }

    private fun handleLogout() {
        sharedPreference.removeValue("Email")
        sharedPreference.removeValue("Password")
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun handleFeedback() {
        // Handle feedback logic here
        // For example, show a dialog, launch a new activity, etc.
        showToast("Feedback button clicked")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
