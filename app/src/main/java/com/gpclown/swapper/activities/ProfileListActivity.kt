package com.gpclown.swapper.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpclown.swapper.Profile
import com.gpclown.swapper.ProfileList
import com.gpclown.swapper.R
import kotlinx.android.synthetic.main.activity_profile_list.*

class ProfileListActivity : AppCompatActivity() {
    private val profileList: ProfileList = ProfileList.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = ProfileAdapter(profileList, this)
    }

    class ProfileViewHolder(inflater: LayoutInflater, parent: ViewGroup, val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.profilerow, parent, false)) {
        private var name: TextView = itemView.findViewById(R.id.pname)
        private var mSwitch: Switch = itemView.findViewById(R.id.switchp)

        fun bind(profile: Profile) {
            name.text = profile.name
            mSwitch.isChecked = profile.active

            name.setOnClickListener{
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("position", adapterPosition)
                intent.putExtra("edit", true)
                context.startActivity(intent)
            }

            mSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    profile.activate(context)
                }
                else {
                    profile.deactivate()
                }
            }
        }
    }

    class ProfileAdapter(private val pList: ProfileList, val context: Context)
        : RecyclerView.Adapter<ProfileViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ProfileViewHolder(inflater, parent, context)
        }

        override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
            val profile: Profile = pList.get(position)
            holder.bind(profile)
        }

        override fun getItemCount(): Int = pList.profiles.size
    }
}
