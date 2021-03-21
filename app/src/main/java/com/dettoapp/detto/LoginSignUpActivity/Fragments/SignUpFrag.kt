package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner.set
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentSignUpBinding
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess


class SignUpFrag : Fragment() {

    private lateinit var viewmodel: LoginSignUpActivityViewModel
    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel =
            ViewModelProvider(requireActivity()).get(LoginSignUpActivityViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()

    }

    private fun initialise() {
        val roles = resources.getStringArray(R.array.Roles)
        binding.spinnerId.apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, roles
            ).apply {
                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            }
        }

        binding.spinnerId.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 1) {
                    binding.etUsn.visibility = View.VISIBLE

                } else {
                    binding.etUsn.visibility = View.GONE

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btnSignUpFrag.setOnClickListener {


            viewmodel.signUpProcess(
                binding.spinnerId.selectedItemPosition,
                binding.etname2.editText?.text.toString(),
                binding.etUsn.editText?.text.toString(),
                binding.etEmail.editText?.text.toString(),
                binding.password.editText?.text.toString(),
                binding.etPassword2.editText?.text.toString()
            )
        }
    }

    private fun liveDataObservers() {
        viewmodel.signup.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showToast(it.message!!)
                    showAlertDialog("Verify Email","come back after verification")

//                    Utility.navigateFragment(
//                        (requireActivity() as BaseActivity).supportFragmentManager,
//                        R.id.loginFragContainer,
//                        LoginFrag(),
//                        "register",
//                        true,
//                        true
//                    )
                }
                is Resource.Error -> {
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    (requireActivity() as BaseActivity).hideProgressBar()
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog("wait...")
                }
            }
        })

    }

    private  fun showAlertDialog(dialogTitle: String,dialogMessage:String){
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle(dialogTitle)
        //set message for alert dialog
        builder.setMessage(dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Ok"){dialogInterface, which ->
            requireActivity().finish();
            exitProcess(0);
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
