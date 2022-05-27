package com.saklayen.techhome.fragments.connectivity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.saklayen.techhome.R
import com.saklayen.techhome.db_action.DataSource
import com.saklayen.techhome.db_action.Table
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.utils.FlagChecker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_connectivity.*

class ConnectivityFragment : Fragment() {
    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1
    var dataSource: DataSource? = null

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val device = Device(1,"Light",25)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connectivity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null) {
            //toast("this device doesn't support bluetooth")
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
        connect.setOnClickListener(View.OnClickListener {
            bluetooth.visibility = View.GONE
            pairedDeviceList()

        })
    }

    private fun pairedDeviceList() {
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (m_pairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
                Log.i("device", ""+device)
            }
        } else {
            //toast("no paired bluetooth devices found")
        }


        val adapter = ArrayAdapter(requireContext(), R.layout.device_item, list)
        paired_list_view.adapter = adapter
        paired_list_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address
            FlagChecker.EXTRA_ADDRESS = address
            /*val intent = Intent(this, HomeFragment::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)*/
            requireActivity().view_pager.currentItem = 1
            requireActivity().nav_bar.visibility = View.VISIBLE
        }
    }

}