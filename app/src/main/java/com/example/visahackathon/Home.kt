package com.example.visahackathon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Home : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.home_to_menu).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Menu)
        }

        view.findViewById<Button>(R.id.home_to_map).setOnClickListener {
            findNavController().navigate(R.id.action_Home_to_Map)
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
    }
}