package io.github.l4ttice.informationexchange

import com.google.firebase.database.ServerValue

class Post() {
    var PID: String = ""
    var title: String = ""
    var contentPost: String = ""
    var UID: String = ""
    val TIMESTAMP = ServerValue.TIMESTAMP
    var count: Int = 0
    var value: Int = 0

    constructor(title: String, contentPost: String, UID: String) : this() {
        this.title = title
        this.contentPost = contentPost
        this.UID = UID
    }
}
