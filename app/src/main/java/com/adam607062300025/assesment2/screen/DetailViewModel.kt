package com.adam607062300025.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adam607062300025.assesment2.database.ContactDao
import com.adam607062300025.assesment2.database.InfoDao
import com.adam607062300025.assesment2.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: ContactDao, private val dao2: InfoDao) : ViewModel() {
    fun insert(nama: String, info: Map<String, String>) {
        val contact = Contact(
            nama    = nama,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(contact)
        }
    }

    fun update(id: Long, nama: String, info: Map<String, String>) {
        val contact = Contact(
            id      = id,
            nama    = nama,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(contact)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    suspend fun getContact(id: Long): Contact? {
        return dao.getContactById(id)
    }
}