package com.dettoapp.detto.UtilityClasses

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Chat.ChatRepository
import com.dettoapp.detto.Chat.ChatViewModel
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentClassDetailViewModel
import com.dettoapp.detto.StudentActivity.ViewModels.StudentHomeFragViewModel
import com.dettoapp.detto.TeacherActivity.Repositories.ClassroomDetailRepository
import com.dettoapp.detto.TeacherActivity.Repositories.TeacherRepository
import com.dettoapp.detto.TeacherActivity.ViewModels.ClassRoomDetailViewModel
import com.dettoapp.detto.TeacherActivity.ViewModels.TeacherHomeFragViewModel
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel
import java.lang.IllegalStateException

class BaseViewModelFactory(private val repository: BaseRepository, private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginSignUpActivityViewModel::class.java) -> LoginSignUpActivityViewModel((repository as LoginSignUpRepository), context) as T
            modelClass.isAssignableFrom(StudentHomeFragViewModel::class.java) -> StudentHomeFragViewModel((repository as StudentRepository), context) as T
            modelClass.isAssignableFrom(TeacherHomeFragViewModel::class.java) -> TeacherHomeFragViewModel((repository as TeacherRepository), context) as T
            modelClass.isAssignableFrom(ClassRoomDetailViewModel::class.java) -> ClassRoomDetailViewModel((repository as ClassroomDetailRepository), context) as T
            modelClass.isAssignableFrom(StudentClassDetailViewModel::class.java) -> StudentClassDetailViewModel((repository as StudentRepository), context) as T
            modelClass.isAssignableFrom(ChatViewModel::class.java) ->  ChatViewModel((repository as ChatRepository)) as T
            else -> throw IllegalStateException("Cant Create ViewModel,No option Base Factory")
        }
    }
}