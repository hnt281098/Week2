package com.example.week2

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class DialogFilter: DialogFragment() {
    var selectedDate: DatePickerDialog.OnDateSetListener? = null

    lateinit var edtDate: EditText
    lateinit var spinner : Spinner
    lateinit var ckbA : CheckBox
    lateinit var ckbF : CheckBox
    lateinit var ckbS : CheckBox
    lateinit var btnSave : Button

    var date : String? = ""
    var spinnerSelect : Int? = 0
    private var CheckedA : Boolean = false
    private var CheckedF : Boolean = false
    private var CheckedS : Boolean = false

    var share : SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.filter, container)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        share = context?.getSharedPreferences("Filter" , Context.MODE_PRIVATE)

        edtDate = view.findViewById(R.id.edtDate)
        spinner = view.findViewById(R.id.spinnerSort)
        ckbA  = view.findViewById(R.id.ckbArts)
        ckbF  = view.findViewById(R.id.ckbFashion)
        ckbS  = view.findViewById(R.id.ckbSports)
        btnSave = view.findViewById(R.id.btnSave)

        getData()

        setData()


        val onChecked : CompoundButton.OnCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
                btnSave.isEnabled = !(ckbA.isChecked == share?.getBoolean("SelectA" , false)!!
                        && ckbF.isChecked == share?.getBoolean("SelectF" , false)!!
                        && ckbS.isChecked == share?.getBoolean("SelectS" , false)!!)

                CheckedA = ckbA.isChecked
                CheckedF = ckbF.isChecked
                CheckedS = ckbS.isChecked
            }

        ckbA.setOnCheckedChangeListener(onChecked)
        ckbF.setOnCheckedChangeListener(onChecked)
        ckbS.setOnCheckedChangeListener(onChecked)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                btnSave.isEnabled = position != share?.getInt("Sort" , 0)
                spinnerSelect = position
            }

        }

        edtDate.setOnClickListener {
            val year: Int
            val month: Int
            val day: Int
            val calender = Calendar.getInstance()

            if(edtDate.text.isNotEmpty()) {
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                calender.time = simpleDateFormat.parse(edtDate.text.toString())
            }

            year = calender.get(Calendar.YEAR)
            month = calender.get(Calendar.MONTH)
            day = calender.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                selectedDate, year, month, day
            ).show()

        }

        selectedDate = DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
            val sim = SimpleDateFormat("dd/MM/yyyy")
//            var d = Calendar.getInstance()

            date = sim.format(sim.parse("$day/$month/$year"))
            btnSave.isEnabled = !date.equals(share?.getString("Date" , ""))
            edtDate.setText(date)
        }

        btnSave.setOnClickListener {
            share = context?.getSharedPreferences("Filter" , Context.MODE_PRIVATE)
            val editor = share?.edit()
            editor?.putString("Date" , date)
            editor?.putInt("Sort" , spinnerSelect!!)
            editor?.putBoolean("SelectA" , CheckedA)
            editor?.putBoolean("SelectF" , CheckedF)
            editor?.putBoolean("SelectS" , CheckedS)
            editor?.apply()
            (context as MainActivity).reloadData()

            dismiss()
        }
    }

    private fun setData() {
        edtDate.setText(share?.getString("Date" , null))
        spinner.setSelection(share?.getInt("Sort" , 0)!!)
        ckbA.isChecked = share?.getBoolean("SelectA" , false)!!
        ckbF.isChecked = share?.getBoolean("SelectF" , false)!!
        ckbS.isChecked = share?.getBoolean("SelectS" , false)!!
    }

    private fun getData() {
        date = share?.getString("Date" , null)
        spinnerSelect = share?.getInt("Sort" , 0)
        CheckedA = share?.getBoolean("SelectA" , false)!!
        CheckedF = share?.getBoolean("SelectF" , false)!!
        CheckedS = share?.getBoolean("SelectS" , false)!!
    }
}
