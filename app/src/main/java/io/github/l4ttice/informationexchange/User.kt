package io.github.l4ttice.informationexchange

import com.google.firebase.database.ServerValue

class User() {
    var UID: String = ""
    var postCount: Int = 0
    var Tokens: Int = 50
    var TokensEarned: Int = 0
    val CreateTime = ServerValue.TIMESTAMP
    var ownedpid: String = ""
    var purchasedpostcount = 0
}