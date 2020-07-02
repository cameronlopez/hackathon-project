package com.example.visahackathon

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home.view.*
import kotlinx.android.synthetic.main.home.view.full_name_text_view
import java.io.InputStream
import java.net.URI
import java.util.*

var globalUser : User = User("",
    "",
    "",
    "",
    0,
    "",
    0.0,
    0.0)

var globalDonation : Donation = Donation(null, null, null)

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Home : Fragment() {

    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase
    lateinit var fStorage : FirebaseStorage

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
        fDatabase.reference.child("/Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        if (it.toString().contains("email=$userEmail")) {
                            val user = it.getValue(User::class.java) as User


                            if (user != null) {
                                globalUser = user
                                Log.d("User", "${globalUser.donateGoal}")
                            }

                            full_name_text_view.text = globalUser.name
                            progressBar.max = globalUser.donateGoal.toInt()
                            progressBar.progress = globalUser.amountDonated.toInt()

                            donation_amount.text = "$${globalUser.amountDonated}0 Donated"

                            if(!globalUser.amountDonated.toString().contains(".")) {
                                donation_amount.text = "$${globalUser.amountDonated}.00 Donated"
                            }

                            if (globalUser.profileImageUrl != "") {
                                Picasso.get().load(globalUser.profileImageUrl)
                                    .into(select_photo_button)
                                select_photo_text.visibility = View.INVISIBLE
                                select_photo_button.background = null
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

            }
        })
//        progressBar.progress = 0
//        donation_amount.text = "$${progressBar.progress} Donated"
//        progressBar.progress += globalUser.amountDonated

        view.findViewById<ImageView>(R.id.select_photo_button).setOnClickListener{

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fStorage = FirebaseStorage.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        if (requestCode == 0 && resultCode == RESULT_OK && data != null ) {
            val uri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)

            //val bitmapDrawable = BitmapDrawable(bitmap)
            select_photo_button.setImageBitmap(bitmap)

            val fileID = UUID.randomUUID().toString()
            fStorage.reference.child("/images/$fileID").putFile(uri!!).addOnSuccessListener {
                fStorage.reference.child("/images/$fileID").downloadUrl.addOnSuccessListener {
                    Log.d("Image", it.toString())

                    val user = User(globalUser.uuid, globalUser.email, globalUser.password, globalUser.name, globalUser.zipCode, it.toString(), globalUser.amountDonated, globalUser.donateGoal)
                    fDatabase.reference.child("/Users/${globalUser.uuid}").setValue(user)
                }
            }

            Log.d("RegisterActivity", "$uri")
        }
    }
}

//class Donation(val Amount: Int = 0, val Email: String = "", val Place: String = "")