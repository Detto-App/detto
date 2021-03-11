package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginViewModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Resource


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
        liveDataObservers()

        val loginBtnId:Button= requireActivity().findViewById(R.id.btnLoginFrag)
        val s1 :EditText =requireActivity().findViewById(R.id.email)
        val s2 :EditText =requireActivity().findViewById(R.id.password)
        loginBtnId.setOnClickListener {
            viewModel.validate(s1.text.toString(),s2.text.toString())
//            Toast.makeText(requireContext(),"feferefre",Toast.LENGTH_LONG).show()
        }

    }
    fun liveDataObservers(){
        viewModel.validate.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->
                {

                }
                is Resource.Error ->{
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }

        })
    }

}