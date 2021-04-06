package com.dettoapp.detto.LinkParseActivity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Db.ProjectDAO
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility

class LinkParseActivity : BaseActivity() {
    private val viewModel: LinkParseViewModel by viewModels(factoryProducer = {
        LinkParserFactory(LinkParserRepository(DatabaseDetto.getInstance(this).classroomDAO, DatabaseDetto.getInstance(this).projectDAO), this.applicationContext)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_parse)

        val incomingIntent = intent
        val data = incomingIntent.data


        liveDataObservers()

        viewModel.validationOfUserWhoClickedTheLink(data.toString())

    }

    @Suppress("RedundantSamConstructor")
    private fun liveDataObservers() {
        viewModel.linkParse.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressDialog()
                    Utility.navigateActivity(this, StudentActivity::class.java)
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
                    showConfirmationDialog(it.data!!, "Do you want to really join?", "" + it.message)
                }
                else -> {
                }
            }
        })
    }

    private fun showConfirmationDialog(type: String, dialogTitle: String, dialogMessage: String) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton("Yes") { _, _ ->
                showToast("Successfully joined the classroom")
                if (type == Constants.TYPE_CID)
                    viewModel.insertClassroom()
                else if (type == Constants.TYPE_PID)
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