package io.github.l4ttice.informationexchange

import com.google.firebase.database.ServerValue

class Post(var title: String, var contentPost: String, var UID: String) {
    val TIMESTAMP = ServerValue.TIMESTAMP
}