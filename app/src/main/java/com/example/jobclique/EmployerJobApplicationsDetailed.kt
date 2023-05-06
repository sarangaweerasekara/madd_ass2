package com.example.jobclique

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EmployerJobApplicationsDetailed : Fragment() {

    private lateinit var id:String
    private lateinit var tvName:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvPhone:TextView
    private lateinit var tvAddedDate:TextView
    private lateinit var tvoptional:TextView
    private lateinit var status : String
    private lateinit var btnupdate : Button

    private lateinit var statusDropDown:Spinner

    private lateinit var selected : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_employer_job_applications_detailed, container, false)

        tvName = view.findViewById(R.id.empJobAName)
        tvEmail = view.findViewById(R.id.empJobAEmail)
        tvPhone = view.findViewById(R.id.empJobAPhone)
        tvAddedDate = view.findViewById(R.id.empJobAAppliedDate)
        tvoptional = view.findViewById(R.id.empJobAOptional)
        statusDropDown = view.findViewById(R.id.statusDropDown)
        btnupdate = view.findViewById(R.id.BtnUpdate)

        setValuesToViews()

        var arrData = arrayOf( status ,"Pending" , "Selected" , "Not Selected" , "Waiting")

        //spinner

        val arrAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,arrData)
        statusDropDown.adapter = arrAdapter

        statusDropDown?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(context , "This applicant is ${arrData[p2]} " , Toast.LENGTH_LONG).show()
                selected = arrData[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context , "Nothing Selected" , Toast.LENGTH_LONG).show()
            }

        }


        btnupdate.setOnClickListener{
            updateStatus( id, selected )

        }

        return view

    }


    private fun setValuesToViews(){
        val arguments = arguments
        if (arguments != null) {

            tvName.text = arguments.getString("name")
            tvEmail.text = arguments.getString("email")
            tvPhone.text = arguments.getString("phoneNo")
            tvAddedDate.text = arguments.getString("ApplicationID")
            tvoptional.text = arguments.getString("optional")
            status = arguments.getString("status").toString()
            id = arguments.getString("id").toString()

        }

    }

    private fun updateStatus(
        id:String ,
        selected:String
    ){
        val mapUpdate = mapOf( "status" to selected )
        val dbRef = FirebaseFirestore.getInstance().collection("JobApplications").document(id)
        dbRef.update(mapUpdate)
            .addOnSuccessListener {
                Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show()

                val fragment = EmployerJobApplications()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.frame_layout_employer,fragment)?.commit()

            }.addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }

    }


}