package com.gpclown.swapper.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpclown.swapper.Profile
import com.gpclown.swapper.ProfileList
import com.gpclown.swapper.R
import com.gpclown.swapper.settings.Setting
import com.gpclown.swapper.triggers.Trigger
import kotlinx.android.synthetic.main.activity_triggers.*

class TriggersActivity : AppCompatActivity() {
    val profileList : ProfileList = ProfileList.getInstance()
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

    class TriggerViewHolder(inflater: LayoutInflater, parent: ViewGroup, val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.profilerow, parent, false)) {
        private var name: TextView = itemView.findViewById(R.id.pname)
        private var mSwitch: Switch = itemView.findViewById(R.id.switchp)

        fun bind(trigger: Trigger) {
            name.text = trigger.type
            mSwitch.isChecked = trigger.state

            name.setOnClickListener{
                val intent = Intent(context, TriggerActivity::class.java)
                context.startActivity(intent)
            }

            mSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    trigger.state = true
                }
                else {
                    trigger.state = false
                }
            }
        }
    }

    class TriggerAdapter(private val profile: Profile?, val context: Context)
        : RecyclerView.Adapter<TriggerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriggerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return TriggerViewHolder(inflater, parent, context)
        }

        override fun onBindViewHolder(holder: TriggerViewHolder, position: Int) {
            val trigger: Trigger = profile!!.getTrigger(position)
            holder.bind(trigger)
        }

        override fun getItemCount(): Int = profile!!.triggers.size
    }
}
