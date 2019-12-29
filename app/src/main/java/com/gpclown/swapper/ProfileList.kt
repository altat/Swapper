package com.gpclown.swapper

class ProfileList private constructor() {
    val profiles: ArrayList<Profile> = ArrayList()

    init {
        ++instanceCounter
    }

    companion object {
        var instanceCounter = 0
        private val mInstance: ProfileList = ProfileList()

        @Synchronized
        fun getInstance() : ProfileList {
            return mInstance
        }
    }

    fun get(position : Int) : Profile {
        return profiles.get(position)
    }

    fun set(position: Int, element: Profile) {
        profiles.set(position, element)
    }

    fun add(element: Profile) {
        profiles.add(element)
    }
    
    fun remove(position: Int) {
        profiles.removeAt(position)
    }

    fun contains(profile: Profile) : Boolean {
        for (el in profiles) {
            if (el === profile) return true
        }

        return false
    }
}