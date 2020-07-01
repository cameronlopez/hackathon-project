package com.example.visahackathon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.menu.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Menu : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menu_email.text = globalUser.email

        if (globalUser.profileImageUrl != "") {
            Picasso.get().load(globalUser.profileImageUrl).into(menu_image)
        }

        view.findViewById<ImageButton>(R.id.menu_to_home).setOnClickListener {
            findNavController().navigate(R.id.action_Menu_to_Home)
        }
    }
}