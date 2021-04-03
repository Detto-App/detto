package com.dettoapp.detto.LinkParseActivity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Db.ProjectDAO
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource

class LinkParseActivity : BaseActivity() {
    private lateinit var viewModel: LinkParseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_parse)

        val factory = LinkParserFactory(LinkParserRepository(DatabaseDetto.getInstance(this).classroomDAO,DatabaseDetto.getInstance(this).projectDAO), this.applicationContext)
        viewModel = ViewModelProvider(this, factory).get(LinkParseViewModel::class.java)

        val incomingIntent = intent
        val data = incomingIntent.data


        liveDataObservers()

        viewModel.validationOfUserWhoClickedTheLink(data.toString())

    }

    private fun liveDataObservers() {
        viewModel.linkParse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressDialog()
                    val intent = Intent(this, StudentActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    super.showAlertDialog("Alert", "" + it.message)
                }
                is Resource.Loading -> {
                    showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {

                    hideProgressDialog()
                    showConfirmationDialog(it.data!!,"Do you want to really join?", "" + it.message)
                }
                else -> {
                }
            }
        })
    }

    private fun showConfirmationDialog(type:String,dialogTitle: String, dialogMessage: String) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton("Yes") { _, _ ->
                showToast("Successfully joined the classroom")
                if(type==Constants.TYPE_CID)
                    viewModel.insertClassroom()
                else if(type == Constants.TYPE_PID)
                    viewModel.insertProject()

                finish()
            }
            setNegativeButton("No") { _, _ ->

                showToast("Request rejected")
                finish()
            }
        }
        val alertDialog: AlertDialog = builder.create().apply {
            setCancelable(false)
        }
        alertDialog.show()
    }

}