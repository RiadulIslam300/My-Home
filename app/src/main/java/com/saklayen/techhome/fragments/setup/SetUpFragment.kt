package com.saklayen.techhome.fragments.setup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saklayen.techhome.R
import com.saklayen.techhome.db_action.DataSource
import com.saklayen.techhome.db_action.Table
import com.saklayen.techhome.fragments.home.HomeFragment
import com.saklayen.techhome.utils.Checker
import kotlinx.android.synthetic.main.fragment_set_up.*

class SetUpFragment : Fragment() {
    private lateinit var ds :DataSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ds = DataSource(requireContext())

        if (Checker.getPreference("on_boarded", requireContext()) != "1"){
            val ds = DataSource(requireContext())
            val deviceMap: java.util.HashMap<String, String> = java.util.HashMap()
            deviceMap[Table.DEVICE_NAME] = "Light"
            deviceMap[Table.DEVICE_POWER] = "0"
            ds.insertDevice(deviceMap)

            val deviceMap1: java.util.HashMap<String, String> = java.util.HashMap()
            deviceMap1[Table.DEVICE_NAME] = "Fan"
            deviceMap1[Table.DEVICE_POWER] = "0"
            ds.insertDevice(deviceMap1)

            val deviceMap2: java.util.HashMap<String, String> = java.util.HashMap()
            deviceMap2[Table.DEVICE_NAME] = "DimLight"
            deviceMap2[Table.DEVICE_POWER] = "0"
            ds.insertDevice(deviceMap2)

            Checker.savePreference("on_boarded", "1", requireContext())
        }

        Log.e("device-->", "onCreateView: "+ds.getDevices("1")["power"])
        light_power.setText(ds.getDevices("1")["power"]).toString()
        fan_power.setText(ds.getDevices("2")["power"]).toString()
        dim_power.setText(ds.getDevices("2")["power"]).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_set_up, container, false)
       // v = view

        return view
    }


}