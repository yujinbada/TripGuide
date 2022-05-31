package fragment.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tripguide.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        btn_tosignup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_tosignup ->{
                Log.d(TAG, "LoginFragment - ontosingupClick() called")
                navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }

    }
}