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
import com.gpclown.swapper.settings.Setting
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private val profileList : ProfileList = ProfileList.getInstance()
    var profile : Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val pNumber = extras.getInt("profile")
            profile = profileList.get(pNumber)
        }

        settingRecycle.layoutManager = LinearLayoutManager(this)
        settingRecycle.adapter = SettingAdapter(profile, this)
    }

    class SettingViewHolder(inflater: LayoutInflater, parent: ViewGroup, val context: Context, val pid: Int) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.profilerow, parent, false)) {
        private var name: TextView = itemView.findViewById(R.id.pname)
        private var mSwitch: Switch = itemView.findViewById(R.id.switchp)

        fun bind(setting: Setting) {
            name.text = setting.name
            mSwitch.isChecked = setting.selected

            name.setOnClickListener{
                val intent = Intent(context, setting.getActivity())
                intent.putExtra("pid", pid)
                context.startActivity(intent)
            }

            mSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                setting.selected = isChecked
            }
        }
    }

    class SettingAdapter(private val profile: Profile?, val context: Context)
        : RecyclerView.Adapter<SettingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return SettingViewHolder(inflater, parent, context, profile!!.id)
        }

        override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
            val setting: Setting = profile!!.getSetting(position)
            holder.bind(setting)
        }

        override fun getItemCount(): Int = profile!!.settings.size
    }
}
