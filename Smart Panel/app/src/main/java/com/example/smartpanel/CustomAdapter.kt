package com.example.smartpanel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.example.smartpanel.model.Item
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CustomAdapter(context: Context, items: List<Item>) :
    ArrayAdapter<Item>(context, 0, items) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val item = getItem(position)

        if (item?.title == "lamp") {
            view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_lamp, parent, false)
            val itemIcon = view.findViewById<ImageView>(R.id.iconlamp)
            val itemSwitch = view.findViewById<Switch>(R.id.switchlamp)

            itemSwitch.setOnCheckedChangeListener(null)
            itemSwitch.isChecked = item.status
            itemSwitch.setOnCheckedChangeListener { _, isChecked ->
                itemIcon.setImageResource(if (isChecked) R.drawable.icon_lamp_on else R.drawable.icon_lamp_off)
                updateDeviceStatus(item.title, "status", isChecked)
            }
            loadDeviceStatus(item.title, itemSwitch, itemIcon)
        } else {
            view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_ac, parent, false)
            val tempSwitch = view.findViewById<Switch>(R.id.switchtemp)

            tempSwitch.setOnCheckedChangeListener { _, isChecked ->
                updateDeviceStatus("ac", "status", isChecked)
            }
            loadDeviceStatus("ac", tempSwitch, null)
        }
        return view
    }

    private fun updateDeviceStatus(devicePath: String, property: String, status: Boolean) {
        database.child("smart_panel/aa101/devices/$devicePath/$property").setValue(status)
    }

    private fun loadDeviceStatus(deviceTitle: String, switchView: Switch, iconView: ImageView?) {
        val path = if (deviceTitle == "ac") "ac/status" else "lamp/status"
        database.child("smart_panel/aa101/devices/$path")
            .get().addOnSuccessListener { dataSnapshot ->
                val status = dataSnapshot.getValue(Boolean::class.java)
                status?.let {
                    switchView.isChecked = it
                    if (deviceTitle == "lamp") {
                        iconView?.setImageResource(if (it) R.drawable.icon_lamp_on else R.drawable.icon_lamp_off)
                    }
                }
            }
    }
}
