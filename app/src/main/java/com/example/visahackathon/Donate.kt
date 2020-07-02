package com.example.visahackathon

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visa.checkout.ManualCheckoutSession.ManualCheckoutLaunchHandler


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Donate : Fragment() {

    lateinit var mView: View
    lateinit var launchHandler: ManualCheckoutLaunchHandler
    var place:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.donate, container, false);
        // Inflate the layout for this fragment
        return mView
//        return inflater.inflate(R.layout.donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup = mView.findViewById<RadioGroup>(R.id.merchantGroup) as RadioGroup

        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                place = view.findViewById<RadioButton>(checkedId).text.toString()
//                place = checkedId.toString()
            }
        })


        view.findViewById<ImageButton>(R.id.donate_to_profile).setOnClickListener {
            findNavController().navigate(R.id.action_Donate_to_Profile)
        }

        val donationAmount:TextView = mView.findViewById(R.id.donationAmount)
        val donationComment:TextView = mView.findViewById(R.id.donationComment)

        view.findViewById<Button>(R.id.donate_to_checkout).setOnClickListener {
            if (!donationAmount.text.toString().equals("") && !place.equals("")) {
                val newDonation = Donation(place, donationAmount.text.toString(), donationComment.text.toString())
                globalDonation = newDonation
                findNavController().navigate(R.id.action_Donate_to_visaCheckoutButton)
            }
            else if (place.equals("")){
                Log.d("Main","Please select a merchant")
                AlertDialog.Builder(context)
                    .setMessage("Please select a merchant")
                    .setPositiveButton("Ok") {dialog, which ->
                        dialog.dismiss()
                    }.show()
            }
            else{
                Log.d("Main","Please enter an amount")
                AlertDialog.Builder(context)
                    .setMessage("Please enter an amount")
                    .setPositiveButton("Ok") {dialog, which ->
                        dialog.dismiss()
                    }.show()
            }
        }
    }
}