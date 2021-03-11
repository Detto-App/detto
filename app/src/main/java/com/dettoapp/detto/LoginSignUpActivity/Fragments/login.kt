package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginViewModel
import com.dettoapp.detto.R


class login : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginBtnId:Button= requireActivity().findViewById(R.id.btnLoginFrag)

        loginBtnId.setOnClickListener {
            Toast.makeText(requireContext(),"feferefre",Toast.LENGTH_LONG).show()
        }
    }
}