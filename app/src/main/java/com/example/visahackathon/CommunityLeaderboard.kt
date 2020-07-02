package com.example.visahackathon

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.community.*
import kotlinx.android.synthetic.main.community.search_bar
import kotlinx.android.synthetic.main.user.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CommunityLeaderboard : Fragment() {

    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase
    lateinit var listView : ListView
    var adapter : CommunityAdapter? = null
    var baseCommunityList = ArrayList<CommunityLeaderboardEntry>()
    var filteredCommunityList = ArrayList<CommunityLeaderboardEntry>()
    lateinit var searchText : EditText

    fun searchList() {
        filteredCommunityList.clear()
        baseCommunityList.forEach {
            if (it.cityName.toLowerCase().contains(search_bar.text.toString().toLowerCase())) {
                filteredCommunityList.add(it)
            }
        }
        adapter!!.updateList(filteredCommunityList)
    }

    fun readData(myCallback: (List<CommunityLeaderboardEntry>) -> Unit) {
        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()
        val orderedUsers = fDatabase.reference.child("/Community").child("zipCode").orderByChild("communityDonated")

        orderedUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children) { // iterates through zip codes in Community
                    val cityName = postSnapshot.child("cityName").value.toString()
                    val cityDonate = postSnapshot.child("communityDonated").value.toString()
                    baseCommunityList.add(CommunityLeaderboardEntry(cityName, cityDonate))
                }
                myCallback(baseCommunityList)
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
        return inflater.inflate(R.layout.community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = listOfCommunities

        readData() {
            adapter = this.context?.let { it1 -> CommunityAdapter(it1, ArrayList(it.reversed())) }
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

        view.findViewById<ImageButton>(R.id.community_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_Community_to_Home)
        }
    }
}