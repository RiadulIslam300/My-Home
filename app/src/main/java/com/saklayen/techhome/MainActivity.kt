package com.saklayen.techhome
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saklayen.techhome.db_action.DataSource
import com.saklayen.techhome.db_action.Table
import com.saklayen.techhome.fragments.about.AboutFragment
import com.saklayen.techhome.fragments.connectivity.ConnectivityFragment
import com.saklayen.techhome.fragments.home.HomeFragment
import com.saklayen.techhome.fragments.report.ReportFragment
import com.saklayen.techhome.fragments.setup.SetUpFragment
import com.saklayen.techhome.utils.Checker
import com.saklayen.techhome.utils.Constants
import com.saklayen.techhome.utils.Constants.DIM_LIGHT_STATUS
import com.saklayen.techhome.utils.Constants.MAIN_LIGHT_STATUS
import com.saklayen.techhome.utils.Constants.REQ_CODE_SPEECH_INPUT
import com.saklayen.techhome.utils.Flags
import com.whiteelephant.monthpicker.MonthPickerDialog
import com.whiteelephant.monthpicker.MonthPickerDialog.OnDateSetListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_set_up.*
import java.io.IOException
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    var ds = DataSource(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        home.tag = "selected"
        reports.tag = "unselected"
        setup.tag = "unselected"
        about.tag = "unselected"

        Log.e("light status check--->", "onCreate: "+ Checker.getPreference("is_light_on", this) )

        var pagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        pagerAdapter.addFragment(ConnectivityFragment())
        pagerAdapter.addFragment(HomeFragment())
        pagerAdapter.addFragment(ReportFragment())
        pagerAdapter.addFragment(SetUpFragment())
        pagerAdapter.addFragment(AboutFragment())

        view_pager.adapter = pagerAdapter
        view_pager.isUserInputEnabled = false

        fab.setOnClickListener {
            promptSpeechInput()
        }


    }

    fun clickAction(view: View){
        if (Flags.collapsed){
            menu_icon.setImageResource(R.drawable.ic_cross)
            home.visibility = View.VISIBLE
            setup.visibility = View.VISIBLE
            reports.visibility = View.VISIBLE
            about.visibility = View.VISIBLE
            Flags.collapsed = false


            if (home.tag == "unselected"){
                home.setImageResource(R.drawable.ic_home)
            }else{
                home.setImageResource(R.drawable.ic_home__select)
            }
            //home.tag = "selected"
        }else{
            menu_icon.setImageResource(R.drawable.ic_menu)
            home.visibility = View.GONE
            setup.visibility = View.GONE
            reports.visibility = View.GONE
            about.visibility = View.GONE
            Flags.collapsed = true
        }
    }

    fun optionClicked(view: View){
        when (view) {
            home -> {
                if (home.tag == "unselected") {
                    Log.e("clicked", "optionClicked: Home" )
                    home.setImageResource(R.drawable.ic_home__select)
                    home.tag = "selected"

                    //others unselected
                    about.setImageResource(R.drawable.ic_info_unselect)
                    about.tag = "unselected"
                    reports.setImageResource(R.drawable.ic_report_unselect)
                    reports.tag = "unselected"
                    setup.setImageResource(R.drawable.ic_setup_unselect)
                    setup.tag = "unselected"
                    // Navigation.findNavController(set_up).navigate(R.id.navigate_to_homeFragment)
                    view_pager.currentItem = 1
                    //view_pager.currentItem = 1


                }
            }
            setup -> {
                if (setup.tag == "unselected") {
                    Log.e("clicked", "optionClicked: Setup" )
                    setup.setImageResource(R.drawable.ic_setup_select)
                    setup.tag = "selected"
                    //others make unselected
                    home.setImageResource(R.drawable.ic_home)
                    home.tag = "unselected"
                    about.setImageResource(R.drawable.ic_info_unselect)
                    about.tag = "unselected"
                    reports.setImageResource(R.drawable.ic_report_unselect)
                    reports.tag = "unselected"
                    //navigate to setup fragmnet
                    //Navigation.findNavController(home_fragment).navigate(R.id.navigate_to_setUpFragment)
                    view_pager.currentItem = 3
                    //view_pager.currentItem = 3
                    //HomeFragment.navigate(R.id.navigate_to_setUpFragment)

                }
            }
            about -> {
                if (about.tag == "unselected") {
                    about.setImageResource(R.drawable.ic_info__select)
                    about.tag = "selected"
                    //others unselected
                    home.setImageResource(R.drawable.ic_home)
                    home.tag = "unselected"
                    reports.setImageResource(R.drawable.ic_report_unselect)
                    reports.tag = "unselected"
                    setup.setImageResource(R.drawable.ic_setup_unselect)
                    setup.tag = "unselected"
                    view_pager.currentItem = 4
                    //view_pager.currentItem = 4
                }
            }
            reports -> {
                if (reports.tag == "unselected") {
                    reports.setImageResource(R.drawable.ic_report_select)
                    reports.tag = "selected"
                    //others unselected
                    setup.setImageResource(R.drawable.ic_setup_unselect)
                    setup.tag = "unselected"
                    home.setImageResource(R.drawable.ic_home)
                    home.tag = "unselected"
                    about.setImageResource(R.drawable.ic_info_unselect)
                    about.tag = "unselected"
                    view_pager.currentItem = 2
                    //view_pager.currentItem = 2
                }
            }
        }


    }

    fun updateDevice(view: View){
        when (view) {
            edit_light->{
                val dataMap = HashMap<String,String>()
                dataMap[Table.DEVICE_POWER] = light_power.text.toString()
                ds.updateDeviceInfo("1", dataMap)
                Toast.makeText(this,"Light power updated",Toast.LENGTH_SHORT).show()
            }
            edit_fan->{
                val dataMap = HashMap<String,String>()
                dataMap[Table.DEVICE_POWER] = fan_power.text.toString()
                ds.updateDeviceInfo("2", dataMap)
                Toast.makeText(this,"Fan power updated",Toast.LENGTH_SHORT).show()
            }
            edit_dim->{
                val dataMap = HashMap<String,String>()
                dataMap[Table.DEVICE_POWER] = dim_power.text.toString()
                ds.updateDeviceInfo("3", dataMap)
                Toast.makeText(this,"Dim Light power updated",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun pickMonthYear(view: View) {
        val calendar = Calendar.getInstance()

        val monthYearPicker = MonthPickerDialog.Builder(
                this,
                MonthPickerDialog.OnDateSetListener { monthOfYear,year -> // TODO Auto-generated method stub

                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    var date = formatValidity(calendar)
                    month_year.text = DateFormatSymbols().months[monthOfYear]
                    //String.format("%.3f", number).toDouble()
                    var lightUnit : Double = String.format("%.5f", ds.retriveTotalSecondsUsed(date,"1").toDouble() * ds.retriveDevicePower("1") / (3600 * 1000) ).toDouble()
                    var lightCost : Double = String.format("%.5f",lightUnit * 5).toDouble() // let per unit cost = 5 tk

                    var fanUnit : Double = String.format("%.5f",(ds.retriveTotalSecondsUsed(date,"2").toDouble() * ds.retriveDevicePower("2")) / (3600 * 1000)).toDouble()
                    var fanCost : Double = String.format("%.5f",fanUnit * 5).toDouble() // let per unit cost = 5 tk

                    var dimUnit : Double = String.format("%.5f",(ds.retriveTotalSecondsUsed(date,"3").toDouble() * ds.retriveDevicePower("3")) / (3600 * 1000)).toDouble()
                    var dimCost : Double = String.format("%.5f",dimUnit * 5).toDouble() // let per unit cost = 5 tk

                    var totalUnit = String.format("%.5f",lightUnit + fanUnit + dimUnit).toDouble()
                    var totalCost = String.format("%.5f",lightCost + fanCost + dimCost).toDouble()

                    light_unitr.text = "$lightUnit KWH"
                    light_cost.text = "$lightCost BDT"

                    fan_unit.text = "$fanUnit KWH"
                    fan_cost.text = "$fanCost BDT"

                    dim_unit.text = "$dimUnit KWH"
                    dim_cost.text ="$dimCost BDT"

                    total_unit.text = "$totalUnit KWH"
                    total_cost.text = "$totalCost BDT"

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)).setMaxYear(calendar.get(Calendar.YEAR)).build().show()

        }
    fun formatValidity(calendar: Calendar): String {
        val myFormat = "MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(calendar.time)
    }

    private fun promptSpeechInput() {
        //Showing google speech input dialog
        //Simply takes user's speech input and returns it to same activity
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        ) //Considers input in free form English
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US)
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "Give Command"
        ) //Text prompt to show to user when asking them to speak
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "Speech Recognization Unavilable", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQ_CODE_SPEECH_INPUT  && resultCode== Activity.RESULT_OK){
            val result=data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            Toast.makeText(this, result?.get(0).toString(), Toast.LENGTH_LONG)
                .show()

            val speechInput=result?.get(0).toString().toLowerCase()
            if(speechInput.contains("all")){
                if(speechInput.contains("light")||speechInput.contains("lights")){
                    if(speechInput.contains("on")||speechInput.contains("one")){
                        //TODO all evices turn on
                    }

                }
            }
            if(speechInput.contains("main light")||speechInput.contains("my light")){
                if(Constants.MAIN_LIGHT_STATUS==false){
                    if(speechInput.contains("one")||speechInput.contains("on") || speechInput.contains("1")){
                        sendCommand("l")
                        Log.e("light status-->", "onViewCreated: "+Checker.getPreference("is_light_on", this) )
                        Checker.savePreference("is_light_on","1",this)
                        Constants.MAIN_LIGHT_STATUS=true
                    }
                }

                if(speechInput.contains("off")||speechInput.contains("of")){
                    if(MAIN_LIGHT_STATUS){
                        sendCommand("l")
                        Checker.savePreference("is_light_on","0",this)
                        MAIN_LIGHT_STATUS=false
                    }

                }
            }

            if(speechInput.contains("dim") ||speechInput.contains("tubelight")){

                if(speechInput.contains("one")||speechInput.contains("on") || speechInput.contains("1")){
                    if(DIM_LIGHT_STATUS==false){
                        sendCommand("d")
                        Log.e("light status-->", "onViewCreated: "+Checker.getPreference("is_light_on", this) )
                        Checker.savePreference("is_dim_on","1",this)
                        DIM_LIGHT_STATUS=true
                    }
                }

                if(speechInput.contains("off")||speechInput.contains("of")){
                    if(DIM_LIGHT_STATUS){
                        sendCommand("d")
                        Checker.savePreference("is_dim_on","0",this)
                        DIM_LIGHT_STATUS=false
                    }

                }
            }
            if (speechInput.contains(" and")) {
                if (speechInput.contains(" on")||speechInput.contains(" one")||speechInput.contains(" 1")) {
                    if (speechInput.contains("a")) {
                        if(MAIN_LIGHT_STATUS==false){
                            sendCommand("l")
                            Log.e("light status-->", "onViewCreated: "+Checker.getPreference("is_light_on", this) )
                            Checker.savePreference("is_light_on","1",this)
                            MAIN_LIGHT_STATUS=true
                        }

                    }

                    if (speechInput.contains(" b")||speechInput.contains(" be")) {
                        if(DIM_LIGHT_STATUS==false){
                            sendCommand("d")
                            Log.e("light status-->", "onViewCreated: "+Checker.getPreference("is_light_on", this) )
                            Checker.savePreference("is_dim_on","1",this)
                            DIM_LIGHT_STATUS=true
                        }

                    }
                    if (speechInput .contains("fan")){

                    }
                }
                if (speechInput.contains(" off")||speechInput.contains(" of")) {
                    if (speechInput.contains("a")) {
                        if(MAIN_LIGHT_STATUS){
                            sendCommand("l")
                            Checker.savePreference("is_light_on","0",this)
                            MAIN_LIGHT_STATUS=false
                        }

                    }
                    if (speechInput.contains("b")) {
                        if(DIM_LIGHT_STATUS){
                            sendCommand("d")
                            Checker.savePreference("is_light_on","0",this)
                            DIM_LIGHT_STATUS=false
                        }

                    }
                    if (speechInput .contains("fan")){

                    }
                }

            }
        }
    }

    private fun sendCommand(input: String) {
        if (HomeFragment.m_bluetoothSocket != null) {
            try{
                HomeFragment.m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

}


