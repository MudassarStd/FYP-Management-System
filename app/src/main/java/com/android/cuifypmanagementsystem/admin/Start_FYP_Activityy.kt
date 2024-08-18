package com.android.cuifypmanagementsystem.admin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.databinding.ActivityStartFypActivityyBinding
import com.android.cuifypmanagementsystem.room.datamodels.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import com.android.cuifypmanagementsystem.room.datamodels.startActivity

class Start_FYP_Activityy : AppCompatActivity() {
    lateinit var binding: ActivityStartFypActivityyBinding
    lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private lateinit var fypheadId: String
    private lateinit var fypsecId: String
    private var keyvalue: String? = null
    private var teacher: Teacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartFypActivityyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set up insets if needed for your layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        displayresult()

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        binding.btnStartActivity.setOnClickListener {
            // Retrieve input values
            val fyphead = binding.etFYPhead.text.toString()
            val fypsec = binding.etFYPSec.text.toString()
            val startyear = binding.etStartYear.text.toString()

            // Validate inputs
            if (fyphead.isEmpty() || fypsec.isEmpty() || startyear.isEmpty()) {
                Toast.makeText(this, "Please fill all the info", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Adding Activity...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            // Create a map with the data to be added to Firestore
            val activityRecord =
                startActivity(fyphead, fypsec, startyear, "0", "08-05-2024 09:47AM")

            // Add the record to Firestore
            db.collection("Activities")
                .add(activityRecord)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Activity has been added", Toast.LENGTH_SHORT).show()
                    // Clear the EditText fields
                    binding.etFYPhead.setText("")
                    binding.etFYPSec.setText("")
                    binding.etStartYear.text.clear()
                    val intent = Intent(this, Started_act_details_activity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Cant't Start Activity", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun displayresult(){
        val fyphead = binding.etFYPhead
        val fypSec = binding.etFYPSec

//        select the fyp head on click
        fyphead.setOnClickListener {
            val intent = Intent(this, DisplayTeacherActivity::class.java)
            intent.putExtra("key", "head")
            startActivity(intent)
        }
//        select the fyp secratory on click
        fypSec.setOnClickListener {
            val intent = Intent(this, DisplayTeacherActivity::class.java)
            intent.putExtra("key", "sec")
            startActivity(intent)
        }
//      get the values of intents that comming from displayTeacherActivity
        keyvalue = intent.getStringExtra("keyvalue")
        teacher = intent.getSerializableExtra("Teacher_Object") as? Teacher

// Ensure teacher is not null before accessing its properties
        if (teacher != null) {
            if (keyvalue == "head") {
                binding.etFYPhead.setText(teacher!!.name)
                val firestoreId = teacher!!.firestoreId
                if (firestoreId != null) {
                    fypheadId = firestoreId
                }
            } else {
                binding.etFYPSec.setText(teacher!!.name)
                val firestoreId = teacher!!.firestoreId
                if (firestoreId != null) {
                    fypsecId = firestoreId
                }
            }
        } else {
            // Handle the case where teacher is null
            Log.e("Error", "Teacher object is null")
        }
    }
}