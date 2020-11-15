package com.gpclown.swapper.settings

import android.bluetooth.BluetoothAdapter
import android.content.Context

class Bluetooth : Setting() {
    override var name: String = "Bluetooth"
    lateinit var bluetoothAdapter: BluetoothAdapter

    override fun activate(context: Context) {
        if (selected) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter.disable()
        }
    }
}