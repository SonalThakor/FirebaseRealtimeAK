package com.example.firebaserealtimeak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaserealtimeak.R
import com.example.firebaserealtimeak.databinding.ActivityInsertionBinding
import com.example.firebaserealtimeak.db.EmployeeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertionBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        binding.btnSave.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {
        // Getting values
        val empName = binding.empNameEditText.text.toString()
        val empAge = binding.empAgeEditText.text.toString()
        val empSalary = binding.empSalaryEditText.text.toString()

        if (empName.isEmpty()) {
            binding.empNameEditText.error = "Please enter name"
            return
        }
        if (empAge.isEmpty()) {
            binding.empAgeEditText.error = "Please enter age"
            return
        }
        if (empSalary.isEmpty()) {
            binding.empSalaryEditText.error = "Please enter salary"
            return
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId, empName, empAge, empSalary)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                binding.empNameEditText.text.clear()
                binding.empAgeEditText.text.clear()
                binding.empSalaryEditText.text.clear()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
