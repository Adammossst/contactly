package com.adam607062300025.assesment2.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adam607062300025.assesment2.R
import com.adam607062300025.assesment2.database.ContactDb
import com.adam607062300025.assesment2.ui.theme.Assesment2Theme
import com.adam607062300025.assesment2.util.ViewModelFactory

const val KEY_ID_CONTACT = "idContact"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Long? = null) {
    val context = LocalContext.current
    val db = ContactDb.getInstance(context)
    val factory = ViewModelFactory(db.dao, db.dao2)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var info by remember { mutableStateOf(emptyMap<String, String>()) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) { return@LaunchedEffect }
        val data = viewModel.getContact(id) ?: return@LaunchedEffect
        nama = data.nama
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (id == null) {
                        Text(
                            text = stringResource(
                                id = R.string.tambah_catatan
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(
                                id = R.string.edit_catatan
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (nama == "" || info.isEmpty()) {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (id == null) {
                                viewModel.insert(nama, info)
                            } else {
                                viewModel.update(id, nama, info)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction {
                            showDialog = true
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        FormMahasiswa(
            nama = nama,
            info = info,
            onNamaChange = {
                nama = it
            },
            onInfoChange = {
                info = it
            },
            modifier = Modifier.padding(padding),
        )

        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                showDialog = false
                viewModel.delete(id)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.hapus)
                    )
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormMahasiswa(nama: String, info: Map<String, String>, onNamaChange: (String) -> Unit, onInfoChange: (Map<String, String>) -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = {
                onNamaChange(it)
            },
            label = { Text(text = stringResource(R.string.nama))  },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assesment2Theme {
        DetailScreen(rememberNavController())
    }
}