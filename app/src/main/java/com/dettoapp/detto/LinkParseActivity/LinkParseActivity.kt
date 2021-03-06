package com.dettoapp.detto.LinkParseActivity

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.DataBaseOperations
import com.dettoapp.detto.TeacherActivity.Fragments.ClassRoomDetailFrag
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility

class LinkParseActivity : BaseActivity(), DataBaseOperations {
    private val viewModel: LinkParseViewModel by viewModels(factoryProducer = {
        LinkParserFactory(
                LinkParserRepository(
                        DatabaseDetto.getInstance(this).classroomDAO,
                        DatabaseDetto.getInstance(this).projectDAO
                ), this.applicationContext
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

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
                    if (it.data == "STUDENT")
                        Utility.navigateActivity(this, StudentActivity::class.java)
                    finish()
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    showAlertDialog("Alert", "" + it.message, "ok", { finish() }, "cancel", { finish() })
                }
                is Resource.Loading -> {
                    showProgressDialog(Constants.MESSAGE_LOADING)
                }
                is Resource.Confirm -> {
                    hideProgressDialog()
                    when (it.data) {
                        Constants.STUDENT.toString() -> {
                            showAlertDialog("Do You Really Want To Join This Class?", it.message!!, yesFunction = {
                                viewModel.insertClassroom(Constants.STUDENT.toString())
                                showToast("Successfully joined the classroom")
                            }, noFunction = {
                                finish()
                            })
                        }
                        Constants.TEACHER.toString() -> {
                            showAlertDialog(
                                    "Do You Want To Review This Class?",
                                    " " + it.message, yesFunction = {
                                Utility.navigateFragment(
                                        supportFragmentManager,
                                        R.id.teacherHomeContainer,
                                        ClassRoomDetailFrag(viewModel.tempClassroom, this),
                                        "detailClassRoom",
                                        false,
                                        false
                                )
                            }
                            )
                        }
                        Constants.TYPE_PID + Constants.STUDENT -> {
                            showAlertDialog(
                                    "Do You Want To Join This Project?",
                                    " " + it.message,
                                    yesFunction = {
                                        viewModel.insertProject()
                                        showToast("Successfully joined the project")
                                        finish()
                                    }, noFunction = {
                                finish()
                            }
                            )
                        }
                        "10${Constants.TEACHER}" -> {
                            showAlertDialog(
                                    "Do You Want To Review this Project",
                                    " " + it.message, yesFunction = {
                                Utility.navigateFragment(
                                        supportFragmentManager,
                                        R.id.teacherHomeContainer,
                                        ClassRoomDetailFrag(viewModel.tempClassroom, this),
                                        "detailClassRoom",
                                        false,
                                        false
                                )
                            }, noFunction = {
                                finish()
                            }
                            )
                        }
//                        "10${Constants.STUDENT}" -> showConfirmationDialog(
//                            it.data!!,
//                            "you have to Join this Class to Join the project ",
//                            " " + it.message
//                        )

                    }
                }
                else -> {
                }
            }
        })

        viewModel.linkParseTeacher.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Utility.navigateFragment(
                            supportFragmentManager,
                            R.id.teacherHomeContainer,
                            ClassRoomDetailFrag(it.data!!, this),
                            "detailClassRoom",
                            false,
                            false
                    )
                }
            }
        })
    }

    private fun showConfirmationDialog(type: String, dialogTitle: String, dialogMessage: String) {

        val builder = AlertDialog.Builder(this)
        val builder2 = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton("Yes") { _, _ ->
//                when (type) {
//                    Constants.STUDENT.toString() -> {
//                        viewModel.insertClassroom(Constants.STUDENT.toString())
//                        showToast("Successfully joined the classroom")
//
//                    }
//                    Constants.TEACHER.toString() -> {
//                        viewModel.insertClassroom(Constants.TEACHER.toString())
//                        showToast("welcome teacher")
//
//                    }
//                    Constants.TYPE_PID + Constants.STUDENT -> {
//                        viewModel.insertProject()
//                        showToast("Successfully joined the project")
//
//
//                    }
//                    "10${Constants.TEACHER}" -> {
                //viewModel.insertClassroom(Constants.TEACHER.toString())
//                        showToast("you can review the project now")
//
//
//                    }
//                    "10${Constants.STUDENT}" -> {
//                        viewModel.insertClassroom(Constants.STUDENT.toString())
//
//
//                    }
//                }
            }
            setNegativeButton("No") { _, _ ->
                showToast("Request rejected")
                finish()
            }
        }


//        val alertDialog: AlertDialog = builder.create().apply {
//            setCancelable(false)
//        }
//        alertDialog.show()
//        with(builder2)
//        {
//            setTitle("join the project now?")
//            setMessage("Do You Wish to Join the project?")
//            setPositiveButton("Yes") { _, _ ->
//                viewModel.insertProject()
//                finish()
//            }
//            setNegativeButton("No") { _, _ ->
//                showToast("Request rejected")
//                finish()
//            }
//
//        }
//        val alertDialog2: AlertDialog = builder2.create().apply {
//            setCancelable(false)
//        }
//        alertDialog2.show()

    }

    override fun onClassRoomDelete(classroom: Classroom) {

    }

}