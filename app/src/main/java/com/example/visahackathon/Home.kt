package com.example.visahackathon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home.view.*
import kotlinx.android.synthetic.main.home.view.full_name_text_view

var globalUser : User = User("",
    "",
    "",
    "",
    "",
    "",
    "")

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Home : Fragment() {

    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        val userEmail : String = fAuth.currentUser?.email.toString();

        //access user
        fDatabase.reference.child("/users").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    if (it.toString().contains("email=$userEmail")) {
                        val user = it.getValue(User::class.java) as User

                        if (user != null) {
                            globalUser = user
                        }

                        Log.d("user", globalUser.fullName)
                        full_name_text_view.text = globalUser.fullName
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        view.findViewById<ImageButton>(R.id.home_to_menu).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Menu)
        }

        view.findViewById<Button>(R.id.home_to_map).setOnClickListener {
            val intent = Intent(activity, Map::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.home_to_community).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_CommunityLeaderboard)
        }

        view.findViewById<Button>(R.id.home_to_user).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_UserLeaderboard)
        }

        view.findViewById<Button>(R.id.home_to_profile).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Profile)
        }

        view.findViewById<Button>(R.id.home_to_network).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Network)
        }

        view.findViewById<Button>(R.id.logout_button).setOnClickListener {
            fAuth.signOut()

            val intent = Intent(activity, Splash::class.java)
            startActivity(intent)
        }
    }
}