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
import com.gpclown.swapper.triggers.Trigger
import kotlinx.android.synthetic.main.activity_triggers.*

class TriggersActivity : AppCompatActivity() {
    private val profileList : ProfileList = ProfileList.getInstance()
    var profile : Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triggers)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val pNumber = extras.getInt("profile")
            profile = profileList.get(pNumber)
        }

        triggerRecycle.layoutManager = LinearLayoutManager(this)
        triggerRecycle.adapter = TriggerAdapter(profile, this)
    }

    class TriggerViewHolder(inflater: LayoutInflater, parent: ViewGroup, val context: Context, val pid: Int) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.profilerow, parent, false)) {
        private var name: TextView = itemView.findViewById(R.id.pname)
        private var mSwitch: Switch = itemView.findViewById(R.id.switchp)
        private val profileList : ProfileList = ProfileList.getInstance()
        var profile : Profile = profileList.get(pid)

        fun bind(trigger: Trigger) {
            name.text = trigger.type
            mSwitch.isChecked = trigger.active

            name.setOnClickListener{
                val intent = Intent(context, trigger.getActivity())
                intent.putExtra("pid", pid)
                context.startActivity(intent)
            }

            mSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    trigger.activate(context, pid)
                    profile.activeTriggers++
                }
                else {
                    trigger.deactivate()
                    trigger.active = false
                    profile.activeTriggers--
                }
            }
        }
    }

    class TriggerAdapter(private val profile: Profile?, val context: Context)
        : RecyclerView.Adapter<TriggerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriggerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return TriggerViewHolder(inflater, parent, context, profile!!.id)
        }

        override fun onBindViewHolder(holder: TriggerViewHolder, position: Int) {
            val trigger: Trigger = profile!!.getTrigger(position)
            holder.bind(trigger)
        }

        override fun getItemCount(): Int = profile!!.triggers.size
    }
}
