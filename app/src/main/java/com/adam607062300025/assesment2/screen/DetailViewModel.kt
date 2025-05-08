package com.adam607062300025.assesment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adam607062300025.assesment2.database.MahasiswaDao
import com.adam607062300025.assesment2.model.Mahasiswa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: MahasiswaDao) : ViewModel() {
    fun insert(nama: String, nim: String, kelas: String) {
        val mahasiswa = Mahasiswa(
            nama    = nama,
            nim     = nim,
            kelas   = kelas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(mahasiswa)
        }
    }

    fun update(id: Long, nama: String, nim: String, kelas: String) {
        val mahasiswa = Mahasiswa(
            id      = id,
            nama    = nama,
            nim     = nim,
            kelas   = kelas
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(mahasiswa)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    suspend fun getMahasiswa(id: Long): Mahasiswa? {
        return dao.getMahasiswaById(id)
    }
}