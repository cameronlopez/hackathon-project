package com.example.visahackathon
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DonationLogAdapter(private val context: Context,
                         private var dataSource: ArrayList<DonationLogEntry>) : BaseAdapter() {

    private val inflater : LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount() : Int {
        return dataSource.size
    }

    override fun getItem(position : Int) : Any {
        return dataSource[position]
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View {
        val rowView = inflater.inflate(R.layout.donation_item, parent, false)
        val businessName = rowView.findViewById(R.id.business) as TextView

        val donationEntry = getItem(position) as DonationLogEntry

        businessName.text = "Donated $${donationEntry.amount} to ${donationEntry.entry}"

        if(!donationEntry.amount.toString().contains(".")) {
            businessName.text = "Donated $${donationEntry.amount}.00 to ${donationEntry.entry}"
        }

        return rowView
    }
}