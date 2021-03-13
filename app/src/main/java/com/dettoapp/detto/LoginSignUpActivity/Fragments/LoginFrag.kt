package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentLoginBinding


class LoginFrag : Fragment() {

    private lateinit var viewModel: LoginSignUpActivityViewModel
    private var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginSignUpActivityViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }

    private fun initialise()
    {
        binding!!.btnLoginFrag.setOnClickListener {
            viewModel.loginProcess(binding!!.email.text.toString(), binding!!.password.text.toString())
        }
    }

    private fun liveDataObservers() {
        viewModel.loginSignUp.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                else -> {

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}