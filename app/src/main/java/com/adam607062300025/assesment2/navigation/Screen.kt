package com.adam607062300025.assesment2.navigation

import com.adam607062300025.assesment2.screen.KEY_ID_CONTACT

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_CONTACT}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}