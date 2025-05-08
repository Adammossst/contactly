package com.adam607062300025.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adam607062300025.assesment2.database.ContactDao
import com.adam607062300025.assesment2.model.Contact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(dao: ContactDao) : ViewModel() {
    val data: StateFlow<List<Contact>> = dao.getContact().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}