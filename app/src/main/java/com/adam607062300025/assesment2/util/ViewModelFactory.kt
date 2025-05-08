package com.adam607062300025.assesment2.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adam607062300025.assesment2.database.ContactDao
import com.adam607062300025.assesment2.database.InfoDao
import com.adam607062300025.assesment2.screen.FormViewModel
import com.adam607062300025.assesment2.screen.MainViewModel

class ViewModelFactory(private val dao: ContactDao, private val dao2: InfoDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            return FormViewModel(dao, dao2) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}