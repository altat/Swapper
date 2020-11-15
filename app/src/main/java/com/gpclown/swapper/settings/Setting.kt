package com.gpclown.swapper.settings

import android.content.Context
import com.gpclown.swapper.activities.TriggersActivity

open class Setting {
    open fun activate(context: Context) {

    }

    open fun getActivity() : java.lang.Class<*> {
        return TriggersActivity::class.java
    }

    open fun reset() {}

    open var selected: Boolean = false
    open var scale: Int = 0
    open var name: String = ""

}