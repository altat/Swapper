package com.gpclown.swapper

import android.content.Context
import android.util.Log
import com.gpclown.swapper.settings.Bluetooth
import com.gpclown.swapper.settings.Setting
import com.gpclown.swapper.settings.Volume
import com.gpclown.swapper.triggers.AudioTrigger
import com.gpclown.swapper.triggers.LocationTrigger
import com.gpclown.swapper.triggers.Trigger
import java.util.*

class Profile {
    var name: String = ""
    var settings: ArrayList<Setting> = ArrayList()
    var triggers: ArrayList<Trigger> = ArrayList()
    var id: Int = 0
    var active: Boolean = false
    var activeTriggers: Int = 0

    constructor() {
        settings.add(Volume())
        settings.add(Bluetooth())


        triggers.add(LocationTrigger())
        triggers.add(AudioTrigger())
    }

    fun activate(context: Context) {
        active = true
        if (activeTriggers == 0) return
        for (trigger in triggers) {
            if (trigger.active && !trigger.pulled) {
                return
            }
        }

        for (setting in settings) {
            setting.activate(context)
        }

        Log.i("SWAPPER", "SETTINGS CHANGED")
    }

    override fun toString() : String {
        return ""
    }

    fun getSetting(position: Int) : Setting {
        return settings.get(position)
    }

    fun getTrigger(position: Int) : Trigger {
        return triggers.get(position)
    }

    fun deactivate() {
        active = false
        for (trigger in triggers) {
            if (trigger.active) {
                trigger.deactivate()
            }
        }
    }
}