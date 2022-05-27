package com.saklayen.techhome.fragments.report

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saklayen.techhome.R
import com.saklayen.techhome.db_action.DataSource

class ReportFragment : Fragment() {
    private lateinit var ds : DataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ds = DataSource(requireContext())

       // Log.e("report light power", "onCreate: kwh: "+ds.retriveDevicePower("1")/1000 )
       // Log.e("report light time", "onCreate: hour: "+ds.retriveTotalSecondsUsed("11/2020", "1")/3600 )

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false)
    }


}