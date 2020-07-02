package com.example.visahackathon

import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.profile.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Profile : Fragment() {

    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase
    lateinit var listView : ListView
    var adapter : DonationLogAdapter? = null
    var donationLogList = ArrayList<DonationLogEntry>()
    var userMap = mutableMapOf<String, String>()


//    fun getTotalDonated(email : String) {
//        fAuth = FirebaseAuth.getInstance()
//        fDatabase = FirebaseDatabase.getInstance()
//
//        fDatabase.reference.child("/Users").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    if(snapshot.children.toString().contains("email=$email")) {
//                        donate_amount.text = snapshot.child("email").value.toString()
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("Error: ", error.toString())
//            }
//        })
//    }

    fun getLeaderboardPositionAndTotalDonated(users : MutableMap<String, String>, currentEmail : String) {
        var position1 : Int = 0
        for(user in users) {
            if(user.key == currentEmail) {
                position1++
                position.text = (position1++).toString()
                donate_amount.text = user.value
                return
            }
            position1++
        }
    }

    fun getUsersForPosition(callback : (MutableMap<String, String>) -> Unit) {

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        val orderedUsers = fDatabase.reference.child("/Users").orderByChild("amountDonated")

        orderedUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children) {
                    val userEmail = postSnapshot.child("email").value.toString()
                    val userDonated = postSnapshot.child("amountDonated").value.toString()
                    userMap.put(userEmail, userDonated)
                }
                callback(userMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error: ", error.toString())
            }
        })
    }

    fun getDonationLog(email : String, callback : (List<DonationLogEntry>) -> Unit) {
        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        fDatabase.reference.child("Donate").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if(it.toString().contains("Email=$email")) {

                        val entry = it.child("Place").value.toString()
                        val amount = it.child("Amount").value.toString()

                        donationLogList.add(DonationLogEntry(entry, amount.toInt()))
                    }
                }
                callback(donationLogList)
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
        return inflater.inflate(R.layout.profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_text_view.text = globalUser.name

        fAuth = FirebaseAuth.getInstance()

        val userEmail = fAuth.currentUser?.email.toString()

        listView = donationLog

        getUsersForPosition {
            getLeaderboardPositionAndTotalDonated(it, userEmail)
        }

        getDonationLog(userEmail) {
            adapter = this.context?.let { it1 -> DonationLogAdapter(it1, ArrayList(it.reversed())) }
            listView.adapter = adapter
        }

        view.findViewById<ImageButton>(R.id.profile_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_Profile_to_Home)
        }

        view.findViewById<Button>(R.id.donation_button).setOnClickListener {
            findNavController().navigate(R.id.action_Profile_to_Donate)
        }
    }
}