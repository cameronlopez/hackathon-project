package com.example.visahackathon
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CommunityAdapter(private val context: Context,
                  private var dataSource: ArrayList<CommunityLeaderboardEntry>) : BaseAdapter() {

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

    fun updateList(filteredCommunityList : ArrayList<CommunityLeaderboardEntry>) {
        dataSource.clear()
        dataSource.addAll(filteredCommunityList)
        notifyDataSetChanged()
    }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View {
        val rowView = inflater.inflate(R.layout.communityleaderboard_item, parent, false)
        val cityName = rowView.findViewById(R.id.cityName) as TextView
        val cityDonate = rowView.findViewById(R.id.raised) as TextView

        val communityEntry = getItem(position) as CommunityLeaderboardEntry

        cityName.text = communityEntry.cityName
        cityDonate.text = "$" + communityEntry.raised + ".00 raised"

        return rowView
    }
}