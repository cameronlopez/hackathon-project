package com.example.visahackathon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.profile.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Profile : Fragment() {

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

        view.findViewById<ImageButton>(R.id.profile_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_Profile_to_Home)
        }

        view.findViewById<Button>(R.id.profile_to_donate).setOnClickListener {
            findNavController().navigate(R.id.action_Profile_to_Donate)
        }
    }
}