package com.dettoapp.detto.LinkParseActivity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Db.ClassroomDatabase
import com.dettoapp.detto.LoginSignUpActivity.Fragments.LoginFrag
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpActivity
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModelFactory
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LinkParseActivity : BaseActivity() {
    private lateinit var viewModel: LinkParseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_parse)

        val factory = LinkParserFactory(LinkParserRepository(ClassroomDatabase.getInstance(this).classroomDAO), this.applicationContext)
        viewModel = ViewModelProvider(this, factory).get(LinkParseViewModel::class.java)


//        if (Firebase.auth.currentUser == null || Firebase.auth.uid == null || Firebase.auth.currentUser?.isEmailVerified==false)
//            startActivity(Intent(this,LoginSignUpActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            })
//        else {
//            val role = getRole(this)
//            val intent = if (role == Constants.TEACHER)
//                Intent(this, TeacherActivity::class.java)
//            else
//                Intent(this, StudentActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            //startActivity(intent)
//        }

        //finish()

        val incomingIntent = intent
        val data = incomingIntent.data
        Toast.makeText(this, "" + data, Toast.LENGTH_SHORT).show();
        viewModel.validationOfUserWhoClickedTheLink(data.toString())
        liveDataObservers()
    }

    private fun liveDataObservers() {
        viewModel.linkParse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    showAlertDialog("Alert",""+it.message)
//                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showProgressDialog(Constants.MESSAGE_LOADING)
                }
                else -> {
                }
            }
        })
    }
    private fun showAlertDialog(dialogTitle: String, dialogMessage: String) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton("Ok") { _, _ ->
                finish();
            }
        }

        val alertDialog: AlertDialog = builder.create().apply {
            setCancelable(false)
            show()
        }

    }
}