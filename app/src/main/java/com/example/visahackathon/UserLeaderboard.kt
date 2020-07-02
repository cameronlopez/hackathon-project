package com.example.visahackathon

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class UserLeaderboard : Fragment() {

    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase
    lateinit var listView : ListView
    var adapter : UserAdapter? = null
    var userNames = ArrayList<String>()
    var userEmails = ArrayList<String>()
    var userDonations = ArrayList<Int>()
    var baseUserList = ArrayList<UserLeaderboardEntry>()
    var filteredUserList = ArrayList<UserLeaderboardEntry>()
    lateinit var searchText : EditText

    fun searchList() {
        filteredUserList.clear()
        baseUserList.forEach {
            if (it.name.contains(search_bar.text.toString())) {
                filteredUserList.add(it)
            }
        }
        adapter!!.updateList(filteredUserList)
    }


//    fun getNames(myCallback: (List<String>) -> Unit) {
//        fAuth = FirebaseAuth.getInstance()
//        fDatabase = FirebaseDatabase.getInstance()
//        val names = fDatabase.reference.child("/users")
//
//        names.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(postSnapshot in snapshot.children) {
//                    val userName = postSnapshot.child("fullName").value.toString()
//                    userNames.add(userName)
//                }
//                myCallback(userNames)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
//
//    fun getEmails(myCallback : (List<String>) -> Unit) {
//        fAuth = FirebaseAuth.getInstance()
//        fDatabase = FirebaseDatabase.getInstance()
//        val emails = fDatabase.reference.child("/users")
//
//        emails.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(postSnapshot in snapshot.children) {
//                    val userEmail = postSnapshot.child("email").value.toString()
//                    userEmails.add(userEmail)
//                }
//                myCallback(userEmails)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//
//    }

//    fun getDonations(myCallback: (List<Int>) -> Unit) {
//
//        fAuth = FirebaseAuth.getInstance()
//        fDatabase = FirebaseDatabase.getInstance()
//        val donations = fDatabase.reference.child("/Donate")
//
//        for(email in userEmails) {
//            donations.addValueEventListener(object : ValueEventListener {
//                var donationAmount = 0
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for(postSnapshot in snapshot.children) {
//                        if(postSnapshot.child("Email").value.toString() == email) {
//                            val amount : Int = postSnapshot.child("Amount").value.toString().toInt()
//                            donationAmount += amount
//                        }
//                    }
//
//                    userDonations.add(donationAmount)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//        }
//
//    }

//    fun getCard(myCallback: (List<UserLeaderboardEntry>) -> Unit) {
//        fAuth = FirebaseAuth.getInstance()
//        fDatabase = FirebaseDatabase.getInstance()
//        val donations = fDatabase.reference.child("/Donate")
//
//        for(emails in userEmails) {
//            donations.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (postSnapshot in snapshot.children) { // iterates through IDs in Users
//                        val userName = postSnapshot.child("fullName").value.toString()
//
//
//                        var userBusiness = postSnapshot.child("Email").value.toString()
//
//                        baseUserList.add(UserLeaderboardEntry(userName, userAmount, userBusiness))
//                    }
//                    myCallback(baseUserList)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//        }
//    }

    fun populateCards(callback : (List<UserLeaderboardEntry>) -> Unit) {

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        val orderedUsers = fDatabase.reference.child("/Users").orderByChild("amountDonated")

        orderedUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children) {
                    val userName = postSnapshot.child("name").value.toString()
                    val userDonated = postSnapshot.child("amountDonated").value.toString()
                    val userZip = postSnapshot.child("zipCode").value.toString()

                    baseUserList.add(UserLeaderboardEntry(userName, userDonated.toFloat(), userZip))
                }
                callback(baseUserList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error: ", error.toString())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = listOfUsers

        populateCards {
            adapter = this.context?.let { it1 -> UserAdapter(it1, ArrayList(it.reversed())) }
            listView.adapter = adapter
        }


        searchText = search_bar

        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                searchList()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){}

        })

        view.findViewById<ImageButton>(R.id.user_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_User_to_Home)
        }
    }
}