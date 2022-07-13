package com.example.athleticsutility

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String? = "",
    var usersurname: String? = "",
    var email: String? = ""
)