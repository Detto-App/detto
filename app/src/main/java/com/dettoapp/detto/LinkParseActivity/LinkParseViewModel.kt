package com.dettoapp.detto.LinkParseActivity

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.Fragments.ClassRoomDetailFrag
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class LinkParseViewModel(private val repository: LinkParserRepository,  private val context: Context):ViewModel() {
    private lateinit var tempClassroom: Classroom
    private lateinit var tempProject:ProjectModel
    private val _linkParse = MutableLiveData<Resource<String>>()
    val linkParse: LiveData<Resource<String>>
        get() = _linkParse
    private val _linkParseTeacher = MutableLiveData<Resource<Classroom>>()
    val linkParseTeacher: LiveData<Resource<Classroom>>
        get() = _linkParseTeacher

    fun validationOfUserWhoClickedTheLink(data:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _linkParse.postValue(Resource.Loading())

                val role = repository.getRole(context)
                authenticate(role)


                Utility.initialiseToken(context = context)
                Utility.initialiseData(role,context)
                val id:String= getID(data)
                val type=getType(data)



                compute(type,id,role)

            }catch (e:Exception){
                _linkParse.postValue(Resource.Error(message = ""+e.localizedMessage))
            }
        }
    }
    private fun authenticate(role :Int){
        if (role == -1) {
            throw Exception("User not logged in!!. Try again after Logging in")
        }
    }
    private fun getID(data:String) = data.substring(data.length-36)


    private fun getType(data:String):String{
        if(data.contains("cid/"))
            return Constants.TYPE_CID
        else if(data.contains("/pid"))
            return Constants.TYPE_PID
        return ""
    }

    private suspend fun getClassroom(role:String,id:String){
        val classroom = RetrofitInstance.createClassroomAPI.getClassroom(id, Utility.TOKEN).body()?:
            throw Exception("Unable to Find Classroom")
        val classRoomDetails="Classroom Name: "+classroom.classroomname+"\nCreated by: "+classroom.teacher.name+"\nSection: "+classroom.section+"\nSem: "+classroom.sem
       tempClassroom=classroom
        _linkParse.postValue(Resource.Confirm(""+role, classRoomDetails))
    }
    private suspend fun getProject(type:String,id:String,role:Int){
        Log.d("WSX","JJHg")

        val project=repository.getSingleProjectDetails(id).body()?:
                throw Exception("Unable to Find This Project room")
        tempClassroom=repository.getClassroom(project.cid)
        if(role==Constants.STUDENT) {
            val susn = repository.getSusn()
            val studentModel = repository.getStudentModel(susn)
            tempProject = project
            if (project.cid in studentModel.classrooms && susn in project.studentList) {
                if(!(project.pid in studentModel.projects)) {
                    val projectDeatils = "Project Name" + project.title + "\nDescription: " + project.desc
                    _linkParse.postValue(Resource.Confirm(type + role, projectDeatils))
                }
                else{
                    _linkParse.postValue(Resource.Error(message = "you have Already Joined the group"))
                }

            } else if (susn in project.studentList) {
                val classroom = RetrofitInstance.createClassroomAPI.getClassroom(project.cid, Utility.TOKEN).body()
                        ?: throw Exception("Class Not Found")
                val classRoomDetails = "Classroom Name: " + classroom.classroomname + "\nCreated by: " + classroom.teacher.name + "\nSection: " + classroom.section + "\nSem: " + classroom.sem
                _linkParse.postValue(Resource.Confirm("10$role", classRoomDetails))

            }
            else{
                _linkParse.postValue(Resource.Error(message = "Sorry ,You Are Not Authorized to Join This Group"))

            }
        }
    else if(role==Constants.TEACHER){
            val projectDeatils = "Project Name" + project.title + "\nDescription: " + project.desc

            _linkParse.postValue(Resource.Confirm("10$role",projectDeatils))

        }

    }



     fun insertClassroom(role:String,cid:String?=""){
         viewModelScope.launch(Dispatchers.IO) {
             try {
                     if (role == "1") {
                         repository.insertClassroom(tempClassroom)
                         val studentModel = Utility.getStudentModel(context)
                         repository.regStudentToClassroom(studentModel, tempClassroom.classroomuid)
                         _linkParse.postValue(Resource.Success("STUDENT"))
                     } else {
                        _linkParseTeacher.postValue(Resource.Success(tempClassroom))
                     }

             }
             catch (e:Exception){
                 _linkParse.postValue(Resource.Error(message = "You have Already Joined The Classroom: "+tempClassroom.classroomname))
             }
         }
    }
//     fun insertClassroom2(role:Int) {
//         viewModelScope.launch(Dispatchers.IO) {
//             try {
//                 val classroom = repository.getClassroom(tempProject.cid)
//                 if(role==Constants.TEACHER){
//                     _linkParseTeacher.postValue(Resource.Success(classroom))
//                 }
//                 else {
//                     repository.insertClassroom(classroom)
//                     val studentModel = Utility.getStudentModel(context)
//                     repository.regStudentToClassroom(studentModel, classroom.classroomuid)
//                     _linkParse.postValue(Resource.Success("STUDENT"))
//                 }
//             } catch (e: Exception) {
//                 _linkParse.postValue(Resource.Error(message = "You have Already Joined The Classroom: " + tempClassroom.classroomname))
//             }
//         }
//     }

    fun insertProject(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                repository.insertProject(tempProject)
                repository.regStudentToProject(tempProject.pid)
            }
            catch (e:Exception){
                _linkParse.postValue(Resource.Error(message = "You have Already Joined This Project Room: "+tempProject.title))
            }

        }
    }

    private suspend fun compute(type:String,id:String,role:Int){
        when(type){
            Constants.TYPE_CID ->getClassroom(""+role,id)
            Constants.TYPE_PID->getProject(type,id,role)
        }
    }

}