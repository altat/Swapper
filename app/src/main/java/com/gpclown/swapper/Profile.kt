package com.gpclown.swapper

import com.gpclown.swapper.settings.Setting
import com.gpclown.swapper.settings.Volume
import com.gpclown.swapper.triggers.LocationTrigger
import com.gpclown.swapper.triggers.Trigger
import java.text.FieldPosition
import java.util.ArrayList

class Profile {
    var name: String = ""
    var settings: ArrayList<Setting> = ArrayList()
    var triggers: ArrayList<Trigger> = ArrayList()
    var number: Int = 0
    var active: Boolean = false

    constructor() {
        settings.add(Volume())
        triggers.add(LocationTrigger())
    }

    fun activate() {
        active = true
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
    }
}