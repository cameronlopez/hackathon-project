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
import android.widget.TextView
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
    lateinit var leaderboardPosition : TextView
    lateinit var donateAmount : TextView
    var adapter : DonationLogAdapter? = null
    var donationLogList = ArrayList<DonationLogEntry>()
    var usersList = ArrayList<String>()


    fun getImpact() {
        val totalDonated = globalUser.amountDonated
        val donateGoal = globalUser.donateGoal
        var percent : Double = 0.0
        if(donateGoal != 0.0) {
            percent = (totalDonated / donateGoal) * 100
        }

        impactText.text = "${percent.toInt().toString()}% Impact"
    }

    fun getTotalDonated(email : String) {
        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        val orderedUsers = fDatabase.reference.child("/Users").orderByChild("amountDonated")

        orderedUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if(it.child("email").value.toString() == email) {
                        donate_amount.text = "$${it.child("amountDonated").value}0"

                        if(!it.child("amountDonated").value.toString().contains(".")) {
                            donate_amount.text = "$${it.child("amountDonated").value}.00"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error: ", error.toString())
            }
        })
    }

    fun getLeaderboardPosition(users : List<String>, currentEmail : String) {
        var position1 : Int = 0
        for(user in users) {
            if(user == currentEmail) {
                position1++
                leaderboardPosition.text = position1.toString()
                return
            }
            position1++
        }
    }

    fun getUsersForPosition(callback : (List<String>) -> Unit) {

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        val orderedUsers = fDatabase.reference.child("/Users").orderByChild("amountDonated")

        orderedUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children) {
                    val userEmail = postSnapshot.child("email").value.toString()
                    usersList.add(userEmail)
                }
                callback(usersList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error: ", error.toString())
            }
        })
    }

    fun getDonationLog(callback : (List<DonationLogEntry>) -> Unit) {
        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        fDatabase.reference.child("Donations/${globalUser.uuid}").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    val entry = it.child("place").value.toString()
                    val amount = it.child("amount").value.toString()

                    donationLogList.add(DonationLogEntry(entry, amount.toDouble()))
                }
                callback(donationLogList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error: ", error.toString())
            }
        })
    }

    fun getCommunityDonated() {

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        fDatabase.reference.child("/Community").child("/zipCode").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if(it.key.toString() == globalUser.zipCode.toString()) {
                        if(it.child("communityDonated").value.toString().contains(".")) {
                            community_amount.text = "$${it.child("communityDonated").value}0"
                        }
                        else {
                            community_amount.text = "$${it.child("communityDonated").value}.00"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

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

        leaderboardPosition = view.findViewById(R.id.leaderboard_position)
        donateAmount = view.findViewById(R.id.donate_amount)

        profile_text_view.text = globalUser.name

        fAuth = FirebaseAuth.getInstance()

        val userEmail = fAuth.currentUser?.email.toString()

        listView = donationLog

        getImpact()

        getTotalDonated(userEmail)

        getUsersForPosition {
            getLeaderboardPosition(it.reversed(), userEmail)
        }

        getCommunityDonated()

        getDonationLog{
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