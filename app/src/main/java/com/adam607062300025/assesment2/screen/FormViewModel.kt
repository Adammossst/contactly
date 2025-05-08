package com.adam607062300025.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adam607062300025.assesment2.database.ContactDao
import com.adam607062300025.assesment2.database.InfoDao
import com.adam607062300025.assesment2.model.Contact
import com.adam607062300025.assesment2.model.Info
import com.adam607062300025.assesment2.model.InfoDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormViewModel(private val dao: ContactDao, private val dao2: InfoDao) : ViewModel() {
    fun insert(nama: String, info: List<InfoDataClass>) {
        val contact = Contact(
            nama    = nama,
        )

        viewModelScope.launch(Dispatchers.IO) {
            val id = dao.insert(contact)
            for (dataClass in info) {
                if (dataClass.type == "") continue
                val infor = Info(
                    contactId = id,
                    type      = dataClass.type,
                    value     = dataClass.value
                )
                dao2.insert(infor)
            }
        }
    }

    fun update(id: Long, nama: String, info: List<InfoDataClass>) {
        val contact = Contact(
            id      = id,
            nama    = nama,
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(contact)
            dao2.deleteById(id)
            for (dataClass in info) {
                if (dataClass.type == "") continue
                val infor = Info(
                    contactId = id,
                    type      = dataClass.type,
                    value     = dataClass.value
                )
                dao2.insert(infor)
            }
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

    suspend fun getInfo(id: Long): List<Info> {
        return dao2.getInfoByContactId(id)
    }
}