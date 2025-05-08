package com.adam607062300025.assesment2.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adam607062300025.assesment2.R
import com.adam607062300025.assesment2.database.ContactDb
import com.adam607062300025.assesment2.model.InfoDataClass
import com.adam607062300025.assesment2.ui.theme.Assesment2Theme
import com.adam607062300025.assesment2.util.ViewModelFactory

const val KEY_ID_CONTACT = "idContact"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Long? = null) {
    val context = LocalContext.current
    val db = ContactDb.getInstance(context)
    val factory = ViewModelFactory(db.dao, db.dao2)
    val viewModel: FormViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var info by remember { mutableStateOf(emptyList<InfoDataClass>()) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) { return@LaunchedEffect }
        val data = viewModel.getContact(id) ?: return@LaunchedEffect
        nama = data.nama
        info = viewModel.getInfo(id).map { InfoDataClass(it.type, it.value) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (id == null) {
                        Text(
                            text = stringResource(
                                id = R.string.tambah_contact
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(
                                id = R.string.edit_contact
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
                            if (nama == "") {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (info.isEmpty()) {
                                Toast.makeText(context, R.string.atleast_1, Toast.LENGTH_LONG).show()
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
        FormContact(
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
fun FormContact(nama: String, info: List<InfoDataClass>, onNamaChange: (String) -> Unit, onInfoChange: (List<InfoDataClass>) -> Unit, modifier: Modifier) {
    val tipe = listOf(
        stringResource(R.string.tipe_nohp),
        stringResource(R.string.tipe_email),
        stringResource(R.string.tipe_alamat),
        stringResource(R.string.tipe_lainnya)
    )

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
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
        OutlinedButton (
            onClick = {
                onInfoChange(info + InfoDataClass(tipe[0], ""))
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_info),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.tambah_info),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        ListInfo(tipe, info, onInfoChange)
    }
}

fun getKeyboardType(type: String): KeyboardType {
    return when (type) {
        "No. HP" -> KeyboardType.Phone
        "Email" -> KeyboardType.Email
        "Alamat" -> KeyboardType.Text
        else -> KeyboardType.Text
    }
}

@Composable
fun ListInfo(listTipe: List<String>, info : List<InfoDataClass>, onInfoChange: (List<InfoDataClass>) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for ((index, value) in info.withIndex()) {
            var selectedOption by remember { mutableStateOf(value.type) }
            var selectedValue by remember { mutableStateOf(value.value) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Dropdown(listTipe, selectedOption) {
                    selectedOption = it
                    onInfoChange(info.mapIndexed { i, data ->
                        if (i == index) {
                            data.copy(type = it, value = "")
                        } else {
                            data
                        }
                    })
                }
                OutlinedTextField(
                    value = value.value,
                    onValueChange = {
                        selectedValue = it
                        onInfoChange(info.mapIndexed { i, data ->
                            if (i == index) {
                                data.copy(value = it)
                            } else {
                                data
                            }
                        })
                    },
                    label = { Text(text = selectedOption) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = getKeyboardType(selectedOption),
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(1f)
                )
                println(getKeyboardType(selectedOption))
                IconButton(
                    onClick = {
                        onInfoChange(info.filterIndexed { i, _ -> i != index })
                    },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.hapus),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(list: List<String>, value: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded},
        modifier = Modifier.widthIn(0.dp, 120.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(text = stringResource(R.string.tipe)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            list.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        expanded = false
                        onValueChange(selectionOption)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assesment2Theme {
        DetailScreen(rememberNavController())
    }
}