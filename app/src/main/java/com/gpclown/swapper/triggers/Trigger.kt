package com.gpclown.swapper.triggers

import android.content.Context
import com.gpclown.swapper.activities.TriggersActivity

open class Trigger {
    open var active: Boolean = false
    open var pulled: Boolean = false
    open var type: String = "trigger"

    open fun activate(context: Context, id: Int) {}

    open fun deactivate() {}

    open fun hasBeenPulled() : Boolean {
        return pulled
    }

    open fun getActivity() : java.lang.Class<*> {
        return TriggersActivity::class.java
    }


}