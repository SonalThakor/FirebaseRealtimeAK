package com.example.firebaserealtimeak.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebaserealtimeak.R
import com.example.firebaserealtimeak.databinding.ActivityEmployeeDetailsBinding
import com.example.firebaserealtimeak.db.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setValuesToViews()

        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                binding.tvEmpId.text.toString(),
                binding.tvEmpName.text.toString()
            )
        }

        binding.btnDelete.setOnClickListener {
            deleteRecord(
                binding.tvEmpId.text.toString()
            )
        }
    }


    private fun setValuesToViews() {
        // Set values using binding object
        binding.apply {
            tvEmpId.text = intent.getStringExtra("empId")
            tvEmpName.text = intent.getStringExtra("empName")
            tvEmpAge.text = intent.getStringExtra("empAge")
            tvEmpSalary.text = intent.getStringExtra("empSalary")
        }
    }

    private fun deleteRecord(
        id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dailog, null)

        mDialog.setView(mDialogView)

        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etEmpName.setText(binding.tvEmpName.text.toString())
        etEmpAge.setText(binding.tvEmpAge.text.toString())
        etEmpSalary.setText(binding.tvEmpSalary.text.toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmpSalary.text.toString()
            )

            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

            // Set updated data using binding object
            binding.apply {
                tvEmpName.text = etEmpName.text.toString()
                tvEmpAge.text = etEmpAge.text.toString()
                tvEmpSalary.text = etEmpSalary.text.toString()
            }

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = EmployeeModel(id, name, age, salary)
        dbRef.setValue(empInfo)
    }
}
