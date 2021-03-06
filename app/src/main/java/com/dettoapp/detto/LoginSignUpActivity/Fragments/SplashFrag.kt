package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Utility

class SplashFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            Utility.navigateFragment(
                requireActivity().supportFragmentManager,
                R.id.loginFragContainer,
                LoginFrag(),
                "login",
                false,
                false
            )
        }, 1000)
    }
}