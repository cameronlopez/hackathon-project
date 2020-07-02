package com.example.visahackathon

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.protobuf.WireFormat
import com.visa.checkout.*
import com.visa.checkout.ManualCheckoutSession.ManualCheckoutLaunchHandler
import com.visa.checkout.Profile
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class VisaCheckoutButton : Fragment() {

    lateinit var mView: View
    lateinit var launchHandler: ManualCheckoutLaunchHandler
    lateinit var fAuth : FirebaseAuth
    lateinit var fDatabase : FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.visacheckout, container, false);

        // Inflate the layout for this fragment
        return mView
//        return inflater.inflate(R.layout.donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCustomButton(
            ConfigurePayment.getProfile(globalDonation.place),
            ConfigurePayment.getPurchaseInfo(globalDonation.amount, globalDonation.comment)
        )

    }

    private fun setUpCustomButton(profile: Profile, purchaseInfo: PurchaseInfo) {

        fAuth = FirebaseAuth.getInstance()
        fDatabase = FirebaseDatabase.getInstance()

        val customButton: Button = mView.findViewById(R.id.custom_button)
        customButton.background = VisaCheckoutSdk.getCardArt(
            this.activity?.applicationContext
        )

        VisaCheckoutSdk.initManualCheckoutSession(this.activity, profile, purchaseInfo, object : ManualCheckoutSession{

            override fun onReady(manualCheckoutLaunchHandler: ManualCheckoutSession.ManualCheckoutLaunchHandler) {
                this@VisaCheckoutButton.launchHandler = manualCheckoutLaunchHandler
            }

            override fun onResult(visaPaymentSummary: VisaPaymentSummary) {
                if (VisaPaymentSummary.PAYMENT_SUCCESS.equals(
                        visaPaymentSummary.getStatusName(), ignoreCase = true
                    )
                ) {
                    Log.d(ContentValues.TAG, "Success")
                    var uTable: DatabaseReference = fDatabase.reference.child("/Users/${globalUser.uuid}/amountDonated")

                    uTable.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var newAmountDonated:Double = globalUser.amountDonated + globalDonation.amount?.toDouble()!!
                            uTable.setValue(newAmountDonated)
                            Log.d("main", globalUser.amountDonated.toString())
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                    val randID = UUID.randomUUID()

                    fDatabase.reference.child("/Donations/${globalUser.uuid}/${randID}").setValue(globalDonation)

                    var msg = ""
                    if (globalUser.amountDonated + globalDonation.amount?.toDouble()!! >= globalUser.donateGoal){
                        msg += "Donation was successful! You have reached your donation goal!"
                    }
                    else{
                        msg += "Donation was successful!"
                    }
                    AlertDialog.Builder(context)
                        .setMessage(msg)
                        .setPositiveButton("Ok") {dialog, which ->
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }.show()
//
                } else if (VisaPaymentSummary.PAYMENT_CANCEL.equals(
                        visaPaymentSummary.getStatusName(), ignoreCase = true)
                )
                {
                    Log.d(ContentValues.TAG, "Canceled")
                    AlertDialog.Builder(context)
                        .setMessage("Donation was Canceled")
                        .setPositiveButton("Ok") {dialog, which ->
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }.show()

                } else if (VisaPaymentSummary.PAYMENT_ERROR.equals(
                        visaPaymentSummary.getStatusName(), ignoreCase = true)
                )
                {
                    Log.d(ContentValues.TAG, "Error")
                    AlertDialog.Builder(context)
                        .setMessage("There was an error with your donation")
                        .setPositiveButton("Ok") {dialog, which ->
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }.show()

                } else if (VisaPaymentSummary.PAYMENT_FAILURE.equals(
                        visaPaymentSummary.getStatusName(), ignoreCase = true
                    )
                ) {
                    Log.d(ContentValues.TAG, "Generic Unknown failure")
                    AlertDialog.Builder(context)
                        .setMessage("Donation failed")
                        .setPositiveButton("Ok") {dialog, which ->
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }.show()
                }
                if (activity != null) {
                    activity!!.runOnUiThread { setUpCustomButton(profile, purchaseInfo) }
                }
            }
        })
        customButton.setOnClickListener {
            if (launchHandler != null) {
                launchHandler.launch()
            }
        }
    }
}