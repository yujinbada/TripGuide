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
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment(), View.OnClickListener{
    val TAG: String = "로그"
    lateinit var navController: NavController //네비게이션 컨트롤러 선언

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "FirstFragment - onCreateView() called")
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        btn_open_login.setOnClickListener(this) //로그인 버튼 클릭
        btn_open_signup.setOnClickListener(this) // 회원가입 버튼 클릭
    }

    override fun onClick(v: View?) { // 클릭시 프래그먼트 이동
        when(v?.id){
            R.id.btn_open_login -> {
                Log.d(TAG, "FirstFragment - onloginClick() called")
                navController.navigate(R.id.action_firstFragment_to_loginFragment)
            }
            R.id.btn_open_signup -> {
                Log.d(TAG, "FirstFragment - onsignClick() called")
                navController.navigate(R.id.action_firstFragment_to_signUpFragment)
            }
        }
    }
}