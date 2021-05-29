package com.dettoapp.detto.TeacherActivity.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.net.MacAddress
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.*
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.UtilityClasses.Constants.toFormattedString
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.UtilityClasses.Mapper
import com.dettoapp.detto.UtilityClasses.Utility.toHashSet
import com.dettoapp.detto.UtilityClasses.Utility.toLowerAndTrim
import com.google.android.gms.tasks.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


@SuppressLint("StaticFieldLeak")
class ClassRoomDetailViewModel(
        private val repository: ClassroomDetailRepository,
        private val context: Context
) : ViewModel() {

    private val TAG = "DDFF"

    private val _classroomStudents = MutableLiveData<Resource<List<StudentModel>>>()
    val classroomStudents: LiveData<Resource<List<StudentModel>>>
        get() = _classroomStudents

    private val _projectList = MutableLiveData<Resource<List<ProjectModel>>>()
    val projectList: LiveData<Resource<List<ProjectModel>>>
        get() = _projectList

    private val _deadline = MutableLiveData<Resource<List<DeadlineModel>>>()
    val deadline: LiveData<Resource<List<DeadlineModel>>>
        get() = _deadline

    private val _rubrics = MutableLiveData<Resource<RubricsModel>>()
    val rubrics: LiveData<Resource<RubricsModel>>
        get() = _rubrics
    private val _marks = MutableLiveData<Resource<ArrayList<MarksModel>>>()
    val marks: LiveData<Resource<ArrayList<MarksModel>>>
        get() = _marks
    private val _projectRubrics = MutableLiveData<Resource<ArrayList<ProjectRubricsModel>>>()
    val projectRubrics: LiveData<Resource<ArrayList<ProjectRubricsModel>>>
        get() = _projectRubrics
    private val _autoProject = MutableLiveData<Resource<ArrayList<ProjectModel>>>()
    val autoProject: LiveData<Resource<ArrayList<ProjectModel>>>
        get() = _autoProject


    fun getClassStudents(classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val classroomStudents = repository.getClassroomStudents(classroom.classroomuid)

                val customComparator = Comparator<StudentModel> { a, b ->
                    val aUsn: Int = a.susn.substring(a.susn.length - 3).toInt()
                    val bUsn: Int = b.susn.substring(a.susn.length - 3).toInt()
                    return@Comparator aUsn.compareTo(bUsn)
                }

                val list = ArrayList(classroomStudents.studentList)
                list.sortWith(customComparator)

                _classroomStudents.postValue(Resource.Success(data = list))
            } catch (e: Exception) {
                _classroomStudents.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getDeadlineFromServer(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val deadline = repository.getDeadline(cid)

                val customComparator = Comparator<DeadlineModel> { a, b ->
                    val aDate: Long = a.fromdate.toLong()
                    val bDate: Long = b.fromdate.toLong()
                    return@Comparator aDate.compareTo(bDate)
                }

                val list = ArrayList(deadline)
                list.sortWith(customComparator)
//                val deadlineModel=DeadlineModel(";dsj","sfddxc","dscx","fdstyre")
//                val list=ArrayList<DeadlineModel>()
//                list.add(deadlineModel)
                _deadline.postValue(Resource.Success(data = deadline))
            } catch (e: Exception) {
                _deadline.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }


    fun getProjects(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val project = repository.getProjects(cid)
                _projectList.postValue(Resource.Success(data = project))
            } catch (e: Exception) {
                _projectList.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }
    }

    fun changeStatus(pid: String, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _projectList.postValue(Resource.Loading())
                repository.changeStatus(pid, status)
                _projectList.postValue((Resource.Confirm(message = "")))
            } catch (e: Exception) {
                _projectList.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun sendNotification(classroom: Classroom) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val x = Notification("Trial", "Trial Message")
                Firebase.messaging.unsubscribeFromTopic("/topics/${classroom.classroomuid}")
                val response = RetrofitInstance.notificationAPI.postNotification(
                    PushNotification(
                        x,
                        "/topics/${classroom.classroomuid}"
                    )
                )
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
    }

    fun getDeadline(
        classroomUid: String,
        dateRangePicker: MaterialDatePicker<Pair<Long, Long>>,
        reason: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (dateRangePicker.selection == null)
                    _deadline.postValue(Resource.Error(message = "Date not selected"))
                if (reason == "")
                    _deadline.postValue(Resource.Error(message = "reason field is empty"))

//                val array=getDates(dateRangePicker.selection)
                val deadlineModel = DeadlineModel(
                    Utility.createID(), reason, dateRangePicker.selection!!.first.toString(),
                    dateRangePicker.selection!!.second.toString()
                )
                repository.createDeadline(deadlineModel, classroomUid)
            } catch (e: Exception) {
                _deadline.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }


    fun storeRubrics(
        titleMap: HashMap<Int, String>,
        marksMap: HashMap<Int, Int>,
        convertMap: HashMap<Int, Int>,
        classid: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _rubrics.postValue(Resource.Loading())
                val marksModelList = ArrayList<MarksModel>()
                for (i in 0..(titleMap.size - 1)) {
                    val marksModel = MarksModel(titleMap[i]!!, marksMap[i]!!, convertMap[i]!!)
                    marksModelList.add(marksModel)
                    Log.d("ASDF", "fdfd")

                }
                Log.d("ASDF", "fdfd")
                val rubricsModel = RubricsModel(
                    Utility.createID(),
                    marksModelList,
                    classid
                )
                repository.insertRubricsToServer(rubricsModel)
                _rubrics.postValue(Resource.Success(data = rubricsModel))
            } catch (e: Exception) {
                _rubrics.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun getRubrics(cid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                _rubrics.postValue(Resource.Loading())
                val rubricsModel = repository.getRubricsFromDAO(cid)
                val titleMarksList = rubricsModel.titleMarksList

                _marks.postValue(Resource.Success(data = titleMarksList!!))

            } catch (e: Exception) {
                _marks.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }

    }

    fun getRubricsForProject(projectModel: ProjectModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var projectRubricsList: ArrayList<ProjectRubricsModel> =
                    repository.getRubricsForProject(projectModel.cid, projectModel.pid)!!
                Log.d("ASD", projectRubricsList.toString())
                if (projectRubricsList.size == 0) {
                    val rubricsModel = repository.getRubricsFromDAO(projectModel.cid)
                    val usnlist = ArrayList<String>(projectModel.studentList)
                    val namelist = ArrayList<String>(projectModel.studentNameList)
                    for (i in 0 until usnlist.size) {
                        projectRubricsList.add(
                            Mapper.mapProjectModelAndRubricsModelToProjectRubricsModel(
                                usnlist[i],
                                projectModel.pid,
                                namelist[i],
                                rubricsModel
                            )
                        )
                    }
//                    Log.d("WSS",projectRubricsList.toString())
                    repository.insertProjectRubricsToServer(projectRubricsList)
                }

                _projectRubrics.postValue(Resource.Success(data = projectRubricsList))
            } catch (e: Exception) {
                _projectRubrics.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

    fun rubricsUpdate(
        studentHashMap: HashMap<String, ArrayList<MarksModel>>,
        projectModel: ProjectModel
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("WXX", studentHashMap.toString())
                val rubricsModel = repository.getRubrics(projectModel.cid)
                Log.d("WQQ", "djhjgf")

                val studentRubricsMap = HashMap<String, RubricsModel>()
                Log.d("WES", studentHashMap.toString() + rubricsModel.toString())
                for (i in studentHashMap.keys) {
                    val tempRubricsModel = Mapper.mapRubricsModelToTempRubricsModel(
                        rubricsModel.rid,
                        studentHashMap[i]!!,
                        rubricsModel.cid
                    )
                    studentRubricsMap[i] = tempRubricsModel
                    Log.d("WQQT", studentRubricsMap.toString())
                }
                Log.d("WQQ", studentRubricsMap.toString())

                repository.updateProjectRubrics(
                    studentRubricsMap,
                    rubricsModel.cid,
                    projectModel.pid
                )
                _marks.postValue(Resource.Confirm(message = "done"))
            } catch (e: Exception) {
                _marks.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }
    }

     fun formTeams(classroom: Classroom) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val teamSize = classroom.settingsModel.teamSize.toInt()
                val students=repository.getClassroomStudents(classroom.classroomuid)
                Log.d("WRR",students.toString())

                val classSize = students.studentList.size
                val studentsModels = ArrayList<StudentModel>(students.studentList)
                var i = 0
                val projectModelList = ArrayList<ProjectModel>()
                val rem = classSize % teamSize
                if(teamSize>classSize){
                    Log.d("WRR","enf")

                    val studenUsnList = HashSet<String>()
                    val studentNameList = ArrayList<String>()
                    for(k in 0 until classSize){
                        Log.d("WRR",studentsModels[k].susn)


                        studenUsnList.add(studentsModels[k].susn)
                        studentNameList.add(studentsModels[k].name)

                    }
                    Log.d("WRR","enf3")

                    val projectModel = Mapper.mapProjectModel(
                            Utility.createID(),
                            studentUsnlist = studenUsnList,
                            tid = classroom.teacher.uid,
                            cid = classroom.classroomuid,
                            studentNameList = studentNameList)
                    projectModelList.add(projectModel)
                }
                else {
                    while (i < (classSize - rem)) {
                        var j = 0
                        val studenUsnList = HashSet<String>()
                        val studentNameList = ArrayList<String>()
                        while (j < teamSize) {
                            Log.d("WRR", i.toString())
                            studenUsnList.add(studentsModels[i + j].susn)
                            studentNameList.add(studentsModels[i + j].name)
                            Log.d("WRR", studenUsnList.toString())
                            Log.d("WRR", j.toString())
                            j += 1
                        }
                        val projectModel = Mapper.mapProjectModel(
                                Utility.createID(),
                                studentUsnlist = studenUsnList,
                                tid = classroom.teacher.uid,
                                cid = classroom.classroomuid,
                                studentNameList = studentNameList
                        )
                        projectModelList.add(projectModel)
                        i += j
                    }
                    while (i < classSize) {
                        Log.d("WRR", teamSize.toString())
                        Log.d("WRR", classSize.toString())

                        val studenUsnList = HashSet<String>()
                        val studentNameList = ArrayList<String>()
                        studenUsnList.add(studentsModels[i].susn)
                        studentNameList.add(studentsModels[i].name)
                        val projectModel = Mapper.mapProjectModel(
                                Utility.createID(),
                                studentUsnlist = studenUsnList,
                                tid = classroom.teacher.uid,
                                cid = classroom.classroomuid,
                                studentNameList = studentNameList
                        )
                        projectModelList.add(projectModel)
                        i += 1

                    }
                }
                Log.d("WRR","enf")
                repository.formTeams(projectModelList,classroom.classroomuid)
                _autoProject.postValue(Resource.Confirm(message = "Done"))
            } catch (e: Exception) {
                _autoProject.postValue(Resource.Error(message = "" + e.localizedMessage))
            }

        }
    }
}

