package io.github.l4ttice.informationexchange

import android.icu.text.SimpleDateFormat
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

object DBUtils {

    val TAG = DBUtils

    fun showSnackbar(id: String, view: View) {
        Snackbar.make(view.findViewById(R.id.parentview), id, Snackbar.LENGTH_LONG).show()
    }

    val databasePosts = FirebaseDatabase.getInstance().getReference("post")
    val databaseUsers = FirebaseDatabase.getInstance().getReference("user")

    val DateFormat: String = "dd-MM-yyyy HH:mm:ss aa"

    fun currentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun fetchUID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    inline fun <reified T> DBFetch(DBref: DatabaseReference): ArrayList<T?> {

        val dataFetched: ArrayList<T?> = ArrayList()
        DBref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w("DB_UTIL.DBFetch", "FETCHED DATA")
                dataFetched.clear()
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: T? = postSnapshot.getValue(T::class.java)
                    dataFetched.add(post)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("DB_UTIL.DBFetch", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
        return dataFetched
    }

    fun updateUser(UID: String, TokensEarned: Int) {
        val TAG = "DB_UTIL.updateUser"
        Log.w(TAG, "updateUser Function Called")
        var Currentuser: User
        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED USER DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val user: User? = postSnapshot.getValue(User::class.java)
                    if (user!!.UID == UID) {
                        Log.w(TAG, "CurrentUser Located")
                        Currentuser = user

                        databaseUsers.child(UID).child("postCount").setValue(Currentuser.postCount + 1)
                        databaseUsers.child(UID).child("tokens").setValue(Currentuser.Tokens + TokensEarned)
                        databaseUsers.child(UID).child("tokensEarned").setValue(Currentuser.TokensEarned + TokensEarned)

                        Log.w(TAG, "User details Updated")

                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    fun checkNewUserv2(UID: String) {
        val TAG = "DB_UTIL.checkNewUserv2"
        Log.w(TAG, "checkNewUserv2 Function Called")
        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var userFound: Boolean = false
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED USER DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val user: User? = postSnapshot.getValue(User::class.java)
                    if (user!!.UID == UID) {
                        Log.w(TAG, "User Exists")
                        userFound = true
                    }
                }
                if (!userFound) {
                    Log.w(TAG, "User Doesn't exist")
                    // IF user isnt in DB create a new entry in DB with default Values
                    val newUser: User = User()
                    newUser.UID = UID
                    newUser.Tokens = 15
                    newUser.postCount = 0
                    newUser.TokensEarned = 0

                    databaseUsers.child(newUser.UID).setValue(newUser)

                    Log.w(TAG, "New User added to DB")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun updatePostContent(view: View, PID: String, UID: String) {
        val TAG = "DB_UTIL.updatePostContent"
        Log.w(TAG, "updatePostContent Function Called")
        databasePosts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED POST DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = postSnapshot.getValue(Post::class.java)
                    if (post!!.PID == PID) {
                        Log.w("DB_UTILS.updatePostContent", "Post Exists")

                        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get Post object and use the values to update the UI
                                Log.w(TAG, "FETCHED USER DATA")
                                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                                    val user: User? = postSnapshot.getValue(User::class.java)
                                    if (user!!.UID == UID) {
                                        Log.w(TAG, "User Exists")
                                        var AlreadyOwned = false
                                        val OwnedPID = user.ownedpid
                                        val Scanner = Scanner(OwnedPID)
                                        while (Scanner.hasNext()) {
                                            var CurrentPID = Scanner.next()
                                            if (CurrentPID == PID) {
                                                Log.w(TAG, "User Owns the Post Already")
                                                AlreadyOwned = true
                                                view.findViewById<TextView>(R.id.postContent).setText(post.contentPost)
                                            }
                                        }
                                        if (!AlreadyOwned) {
                                            view.findViewById<TextView>(R.id.postContent).text = post.contentPost.substring(0, Math.min(post.contentPost.length, 250)) +
                                                    "...\n\nPURCHASE Post to view the Remaining parts of the Post."
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            }
                        })

                        //view.findViewById<TextView>(R.id.postContent).setText(post.contentPost)

                        Log.w("DB_UTILS.updatePostContent", "Post Heading updated")
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun updatePostandAuthorDetailsPOST(view: View, PID: String) {
        val TAG = "DB_UTIL.updatePostandAuthorDetailsPOST"
        Log.w(TAG, "updatePostandAuthorDetails Function Called")
        databasePosts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED POST DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = postSnapshot.getValue(Post::class.java)
                    if (post!!.PID == PID) {
                        view.findViewById<TextView>(R.id.author_fpd).setText(post.UID)
                        view.findViewById<TextView>(R.id.fpd_count).setText(post.count.toString())
                        //Log.w("DB_UTILS.updatePostandAuthorDetails","PPM  :"+post.timestamp.toString())
                        //view.findViewById<TextView>(R.id.dateview_fpd).text = DBUtils.GetHumanReadableDate(post.timestamp.toLong())
                        view.findViewById<TextView>(R.id.tokens_fpd).text = post.value.toString()


                        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get Post object and use the values to update the UI
                                Log.w(TAG, "FETCHED USER DATA")
                                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                                    val user: User? = postSnapshot.getValue(User::class.java)
                                    if (user!!.UID == post.UID) {
                                        Log.w(TAG, "User Exists")
                                        Log.w(TAG, "User has posted " + user.postCount)
                                        view.findViewById<TextView>(R.id.postcountviewer).setText(user.postCount.toString())
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            }
                        })

                        Log.w(TAG, "Post Status updated")
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    fun updatePostDetailsUI(view: View, PID: String) {
        val TAG = "DB_UTIL.updatePostDetailsUI"
        Log.w(TAG, "updatePostDetailsUI Function Called")
        databasePosts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED USER DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = postSnapshot.getValue(Post::class.java)
                    if (post!!.PID == PID) {
                        Log.w(TAG, "Post Exists")

                        view.findViewById<TextView>(R.id.postTitle).setText(post.title)
                        view.findViewById<TextView>(R.id.tokenview).setText(post.value.toString())
                        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                        Log.w(TAG, "Post Heading updated")
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun ProcessPurchase(UID: String, PID: String, view: View) {
        val TAG = "DB_UTIL.ProcessPurchase"
        Log.w(TAG, "ProcessPurchase Function Called")

        var TokensRequired = 0
        var CurrentUser: User = User()

        databasePosts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED USER DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = postSnapshot.getValue(Post::class.java)
                    if (post!!.PID == PID) {
                        Log.w(TAG, "Post Exists")

                        TokensRequired = post.value
                        Log.w(TAG, "Tokens Required Fetched")

                        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get Post object and use the values to update the UI
                                Log.w(TAG, "FETCHED USER DATA")
                                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                                    val user: User? = postSnapshot.getValue(User::class.java)
                                    if (user!!.UID == UID) {
                                        Log.w(TAG, "CurrentUser Located")
                                        CurrentUser = user
                                        val AvailabaleTokens = CurrentUser.Tokens
                                        val OwnedPID = CurrentUser.ownedpid

                                        val Scanner = Scanner(OwnedPID)
                                        var alreadyOwned = false
                                        //Log.w(TAG, "PURCHASE PID = "+PID)
                                        while (Scanner.hasNext()) {
                                            val CurrentElem = Scanner.next()
                                            if (CurrentElem == PID) {
                                                //Log.w(TAG, "PURCHASE Current Elem :" +CurrentElem)
                                                alreadyOwned = true
                                                //Log.w(TAG, "Already Oned = "+alreadyOwned)
                                            }
                                        }

                                        if (AvailabaleTokens >= TokensRequired && !alreadyOwned) {
                                            databaseUsers.child(UID).child("ownedpid").setValue(OwnedPID + " " + PID)
                                            databaseUsers.child(UID).child("tokens").setValue(AvailabaleTokens - TokensRequired)
                                            databaseUsers.child(UID).child("purchasedpostcount").setValue(CurrentUser.purchasedpostcount + 1)

                                            showSnackbar("Purchase Successfull", view)
                                            Log.w(TAG, "User details Updated")

                                            DBUtils.updatePostandAuthorDetailsPOST(view, PID)
                                        } else if (alreadyOwned) {
                                            showSnackbar("Already Purchased", view)
                                        } else {
                                            showSnackbar("Insufficient Tokens", view)
                                        }
                                    }
                                }

                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            }
                        })
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    fun GetHumanReadableDate(epochSec: Long): String {
        val TAG = "DB_UTIL.GetHumanReadableDate"
        Log.w(TAG, "Recieved input : " + epochSec)
        val date = Date(epochSec * 1000)
        val format = SimpleDateFormat(DateFormat, Locale.getDefault())
        Log.w(TAG, "Calculated epoch timestamp")
        return format.format(date)
    }

    fun PostDetailsUIHandler(view: View, PID: String) {
        val UID = DBUtils.fetchUID()
        val TAG = "DB_UTIL.PostDetailsUIHandler"

        databasePosts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.w(TAG, "FETCHED USER DATA")
                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                    val post: Post? = postSnapshot.getValue(Post::class.java)
                    if (post!!.PID == PID) {
                        Log.w(TAG, "Post Exists")

                        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get Post object and use the values to update the UI
                                Log.w(TAG, "FETCHED USER DATA")
                                for (postSnapshot: DataSnapshot in dataSnapshot.children) {
                                    val user: User? = postSnapshot.getValue(User::class.java)
                                    if (user!!.UID == UID) {
                                        Log.w(TAG, "CurrentUser Located")
                                        val OwnedPID = user.ownedpid

                                        val Scanner = Scanner(OwnedPID)
                                        var alreadyOwned = false
                                        //Log.w(TAG, "PURCHASE PID = "+PID)
                                        while (Scanner.hasNext()) {
                                            val CurrentElem = Scanner.next()
                                            if (CurrentElem == PID) {
                                                //Log.w(TAG, "PURCHASE Current Elem :" +CurrentElem)
                                                alreadyOwned = true
                                                //Log.w(TAG, "Already Oned = "+alreadyOwned)
                                            }
                                        }
                                        if (alreadyOwned) {
                                            view.findViewById<Button>(R.id.purchaseButton).visibility = android.view.View.GONE
                                            view.findViewById<Button>(R.id.cancelButton).text = "Back"
                                            showSnackbar("Post already Purchased.", view)
                                            Log.w(TAG, "Already Owned")
                                            Log.w(TAG, "Disabled PurchaseButton")
                                        }
                                    }
                                }

                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            }
                        })
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}