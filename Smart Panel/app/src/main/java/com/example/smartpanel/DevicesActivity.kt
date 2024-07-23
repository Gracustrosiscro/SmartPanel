package com.example.smartpanel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpanel.model.Item
import com.google.firebase.database.*

class DevicesActivity : AppCompatActivity() {
    private var listView: ListView? = null
    private var items: MutableList<Item>? = null
    private var adapter: CustomAdapter? = null
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        database = FirebaseDatabase.getInstance().reference

        listView = findViewById(R.id.list_view)
        items = ArrayList()
        items!!.add(Item("lamp", false))
        items!!.add(Item("ac", false))

        adapter = CustomAdapter(this, items as ArrayList<Item>)
        listView?.adapter = adapter

        listView?.setOnItemClickListener { _, _, position, _ ->
            val selectedItem: Item = items!![position]
            Toast.makeText(this@DevicesActivity, selectedItem.title, Toast.LENGTH_SHORT).show()
        }

        database.child("smart_panel/aa101/devices/lamp/status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val status = dataSnapshot.getValue(Boolean::class.java) ?: false
                items?.get(0)?.status = status
                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        database.child("smart_panel/aa101/devices/ac/status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val status = dataSnapshot.getValue(Boolean::class.java) ?: false
                items?.get(1)?.status = status
                adapter?.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        database.child("smart_panel/aa101/devices/ac/temperature").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temp = dataSnapshot.getValue(Int::class.java) ?: 0
                findViewById<TextView>(R.id.celcius).text = "$temp°C"
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val switchLamp = findViewById<Switch>(R.id.switchlamp)
        switchLamp?.setOnCheckedChangeListener { _, isChecked ->
            database.child("smart_panel/aa101/devices/lamp/status").setValue(isChecked)
        }

        val switchTemp = findViewById<Switch>(R.id.switchtemp)
        switchTemp?.setOnCheckedChangeListener { _, isChecked ->
            database.child("smart_panel/aa101/devices/ac/status").setValue(isChecked)
        }
    }
}
