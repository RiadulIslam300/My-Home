package com.saklayen.techhome.fragments.home

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.saklayen.techhome.MainActivity
import com.saklayen.techhome.R
import com.saklayen.techhome.db_action.DataSource
import com.saklayen.techhome.db_action.Table
import com.saklayen.techhome.utils.Checker
import com.saklayen.techhome.utils.FlagChecker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    companion object {
        lateinit var fanSwitch : SwitchMaterial
        lateinit var lightSwitch : SwitchMaterial
        lateinit var dimtSwitch : SwitchMaterial
        lateinit var autotSwitch : SwitchMaterial
        lateinit var fanSeek : SeekBar
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String

        fun showPopUp(context: Context){
            val builder = AlertDialog.Builder(context)
            //set title for alert dialog
            builder.setTitle("Onboard Set Up")
            //set message for alert dialog
            builder.setMessage("Set up your device configuration")
            builder.setIcon(android.R.drawable.ic_input_add)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface , which ->
                //context.ac().view_pager.currentItem = 3
                (context as Activity).view_pager.currentItem = 3
            }

            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //nav_bar.isEnabled = false
        lightSwitch = view.findViewById(R.id.light_switch)
        fanSwitch = view.findViewById(R.id.fan_switch)
        dimtSwitch = view.findViewById(R.id.dim_switch)
        autotSwitch = view.findViewById(R.id.auto_switch)
        fanSeek = view.findViewById(R.id.fan_seek)
        // un comment
        light_switch.isEnabled = false
        fan_switch.isEnabled = false
        dim_switch.isEnabled = false
        auto_switch.isEnabled = false
        fan_seek.isEnabled = false
        //var  viwModel = ViewModelProvider.AndroidViewModelFactory.getInstance(context?.applicationContext as Application)
        m_address = FlagChecker.EXTRA_ADDRESS
        ConnectToDevice(requireContext()).execute()

            if (Checker.getPreference("is_light_on", requireContext()) == "1"){
                Log.e("light status2 switch->", "onViewCreated: "+Checker.getPreference("is_light_on", requireContext()) )
                light_switch.isChecked = true;
            }
            if (Checker.getPreference("is_fan_on", requireContext()) == "1"){
                fan_switch.isChecked = true;
                fan_seek.progress = Checker.getPreference("ssek_to", requireContext())!!.toInt()
            }
            if (Checker.getPreference("is_dim_on", requireContext()) == "1"){
                dim_switch.isChecked = true;
            }

            light_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                sendCommand("l")
                if (isChecked){
                    Checker.savePreference("is_light_on", "1", requireContext())
                    Log.e("light status-->", "onViewCreated: "+Checker.getPreference("is_light_on", requireContext()) )
                    insertStartTime("1")
                }
                else{
                    Checker.savePreference("is_light_on", "0", requireContext())
                    insertEndTime("1")
                }

            })
            fan_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                sendCommand("f")
                if (isChecked){
                    Checker.savePreference("is_fan_on", "1", requireContext())
                    // insert report
                    insertStartTime("2")
                    fan_seek.isEnabled = true
                }
                else{
                    Checker.savePreference("is_fan_on", "0", requireContext())
                    // insert report
                    insertEndTime("2")
                    fan_seek.isEnabled = false
                    Checker.savePreference("seek_to","0", requireContext())
                }

            })


            fan_seek.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar,
                                               progress: Int, fromUser: Boolean) {
                    // write custom code for progress is changed
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    sendCommand(fan_seek.progress.toString())

                    when (fan_seek.progress) {
                        in 1..20 -> sendCommand("m")
                        in 22..40 -> sendCommand("n")
                        in 42..60 -> sendCommand("0")
                        in 62..80 -> sendCommand("p")
                        in 82..100 -> sendCommand("q")
                    }
                    Checker.savePreference("seek_to",(fan_seek.progress.toString()), requireContext())
                    Toast.makeText(requireContext(),
                            "Fan speed is: " + fan_seek.progress + "%",
                            Toast.LENGTH_SHORT).show()
                }

            })
            dim_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                sendCommand("d")
                if (isChecked){
                    Checker.savePreference("is_dim_on", "1", requireContext())
                    // insert report
                    insertStartTime("3")
                }
                else{
                    Checker.savePreference("is_dim_on", "0", requireContext())
                    // insert report
                    insertEndTime("3")
                }

            })

            auto_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked){
                    sendCommand("a")
                    fan_switch.isChecked = false
                    light_switch.isChecked = false
                    dim_switch.isChecked = false
                    light_switch.isEnabled = false
                    fan_switch.isEnabled = false
                    dim_switch.isEnabled = false
                    fan_seek.isEnabled = false
                }
                else{
                    sendCommand("a")
                    light_switch.isEnabled = true
                    fan_switch.isEnabled = true
                    dim_switch.isEnabled = true
                    //fan_seek.isEnabled = true
                }

            })


            //fan_seek.isEnabled = true


        //control_led_disconnect.setOnClickListener { disconnect() }
    }

    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /*private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }*/

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
                Toast.makeText(context,"Not Connected !", Toast.LENGTH_SHORT).show()
            } else {
                m_isConnected = true
                Checker.connected = true
                fanSwitch.isEnabled = true
                lightSwitch.isEnabled = true
                dimtSwitch.isEnabled = true
                autotSwitch.isEnabled = true
                Toast.makeText(context,"Connected", Toast.LENGTH_SHORT).show()
                if (Checker.getPreference("on_boarded", context) != "1"){
                    showPopUp(context)
                }

            }
            m_progress.dismiss()
        }
    }

    fun insertStartTime(deviceId: String){

        val ds = DataSource(requireContext())

        val my: DateFormat = SimpleDateFormat("MM/YYYY")
        val monthYear: String = my.format(Date())
        val d: DateFormat = SimpleDateFormat("dd")
        val day: String = d.format(Date())
        val currentTime: Date = Calendar.getInstance().time
        //val t: DateFormat = SimpleDateFormat("HH:mm:ss")
        val t: DateFormat = SimpleDateFormat("HH:mm:ss")
        val  time = t.format(Date())
        val dataMap = HashMap<String,String>()
        dataMap[Table.DEVICE_ID] = deviceId
        dataMap[Table.YEAR_MONTH] = monthYear
        dataMap[Table.DAY] = day
        dataMap[Table.START_TIME] = time
        dataMap[Table.END_TIME] = "0"
        ds.insertReport(dataMap);
    }

    fun insertEndTime(deviceId: String){
        val ds = DataSource(requireContext())
        val reportId = ds.retriveReportId(deviceId)
        //val t: DateFormat = SimpleDateFormat("HH:mm:ss")
        val t: DateFormat = SimpleDateFormat("HH:mm:ss")
        val  time = t.format(Date())
        val dataMap = HashMap<String,String>()
        dataMap[Table.END_TIME] = time
        ds.updateReport(reportId,dataMap)
    }


}
