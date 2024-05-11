package com.example.taskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.database.ToDo

class MainActivityData:ViewModel() {
    private  val _data = MutableLiveData<List<ToDo>>()

    val data:LiveData<List<ToDo>> = _data

    fun setData(data:List<ToDo>){
        _data.value = data
    }

}