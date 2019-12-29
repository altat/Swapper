package com.gpclown.swapper.triggers

open class Trigger {
    open var state: Boolean = false
    open var type: String = "trigger"

    open fun hasBeenPulled() : Boolean {
        return false
    }
}