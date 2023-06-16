package com.example.firebaserealtimeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebaserealtimeak.R
import com.example.firebaserealtimeak.db.EmployeeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var empNameEditText: EditText
    private lateinit var empAgeEditText: EditText
    private lateinit var empSalaryEditText: EditText
    private lateinit var btnSave: Button

    private lateinit var dbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        empNameEditText = findViewById(R.id.empNameEditText)
        empAgeEditText = findViewById(R.id.empAgeEditText)
        empSalaryEditText = findViewById(R.id.empSalaryEditText)
        btnSave = findViewById(R.id.btnSave)

         dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        btnSave.setOnClickListener{
            saveEmployeeData()
        }
    }

    private  fun saveEmployeeData() {
        //getting values
        val empName = empNameEditText.text.toString()
        val empAge = empAgeEditText.text.toString()
        val empSalary = empSalaryEditText.text.toString()

        if (empName.isEmpty()) {
            empNameEditText.error = "Please enter name"
        }
        if (empAge.isEmpty()) {
            empAgeEditText.error = "Please enter age"
        }
        if (empSalary.isEmpty()) {
            empSalaryEditText.error = "Please enter salary"
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId, empName, empAge, empSalary)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                empNameEditText.text.clear()
                empAgeEditText.text.clear()
                empSalaryEditText.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}