package com.example.visahackathon
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UserAdapter(private val context: Context,
                  private var dataSource: ArrayList<UserLeaderboardEntry>) : BaseAdapter() {

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

    fun updateList(filteredUserList : ArrayList<UserLeaderboardEntry>) {
        dataSource.clear()
        dataSource.addAll(filteredUserList)
        notifyDataSetChanged()
    }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View {
        val rowView = inflater.inflate(R.layout.userleaderboard_item, parent, false)
        val userName = rowView.findViewById(R.id.user) as TextView
        val userAmount = rowView.findViewById(R.id.amount) as TextView
        val userBusiness = rowView.findViewById(R.id.business) as TextView

        val userEntry = getItem(position) as UserLeaderboardEntry

        userName.text = userEntry.name

        if(userEntry.amount.toString().contains(".")) {
            userAmount.text = "$${userEntry.amount}0 raised"
        }
        else {
            userAmount.text = "$${userEntry.amount}.00 raised"
        }

        userBusiness.text = "Located in " + userEntry.zipCode

        return rowView
    }
}