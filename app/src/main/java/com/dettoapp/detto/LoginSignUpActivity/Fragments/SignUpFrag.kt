package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.Resource


class SignUpFrag : Fragment() {
    private lateinit var viewmodel: LoginSignUpActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(requireActivity()).get(LoginSignUpActivityViewModel::class.java)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val email: EditText = requireActivity().findViewById(R.id.et_email)
        val password: EditText = requireActivity().findViewById(R.id.password)
        val btn: Button = requireActivity().findViewById(R.id.btn_SignUpFrag)
        val roles = resources.getStringArray(R.array.Roles)
        val spinner: Spinner = requireActivity().findViewById(R.id.spinner_id)
        val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter

        liveDataObservers()

        btn.setOnClickListener {
            viewmodel.signUpProcess(email.text.toString(), password.text.toString())
        }
    }

    private fun liveDataObservers() {
        viewmodel.loginSignUp.observe(viewLifecycleOwner, {
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
}
