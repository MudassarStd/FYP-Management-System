package com.android.cuifypmanagementsystem.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.adapters.selectTeachersforFYPheadSEC
import com.android.cuifypmanagementsystem.databinding.ActivityDisplayTeacherBinding
import com.android.cuifypmanagementsystem.room.datamodels.Teacher

class DisplayTeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayTeacherBinding
    private lateinit var teacherRecyclerView: RecyclerView
    private lateinit var selectTeacherAdapter: selectTeachersforFYPheadSEC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDisplayTeacherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        teacherRecyclerView = binding.Teacherrecyclerview
        teacherRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample list of teachers
        val teacherList = listOf(
            Teacher(
                firestoreId = "f1x3p9v7",
                name = "John Doe",
                email = "john.doe@example.com",
                depart = "Mathematics",
                role = "Professor",
                registrationTimeStamp = 1634854800000L
            ),
            Teacher(
                firestoreId = "a2b4c6d8",
                name = "Jane Smith",
                email = "jane.smith@example.com",
                depart = "Physics",
                role = "Assistant Professor",
                registrationTimeStamp = 1650289200000L
            ),
            Teacher(
                firestoreId = "k3m5n7p1",
                name = "Alice Johnson",
                email = "alice.johnson@example.com",
                depart = "Chemistry",
                role = "Lecturer",
                registrationTimeStamp = 1622544000000L
            ),
            Teacher(
                firestoreId = "x8y9z0a1",
                name = "Bob Brown",
                email = "bob.brown@example.com",
                depart = "Biology",
                role = "Researcher",
                registrationTimeStamp = 1640995200000L
            ),
            Teacher(
                firestoreId = "l5m6n7o8",
                name = "Emily Davis",
                email = "emily.davis@example.com",
                depart = "Computer Science",
                role = "Senior Lecturer",
                registrationTimeStamp = 1609459200000L
            )
        )
        // Set up adapter
        selectTeacherAdapter = selectTeachersforFYPheadSEC(this, teacherList)
        teacherRecyclerView.adapter = selectTeacherAdapter

        val keyvalue = intent.getStringExtra("key")

        selectTeacherAdapter.setOnItemClickListner(object :
            selectTeachersforFYPheadSEC.onItemClickListner {
            override fun onItemClick(position: Int) {
                val selectedTeacher = teacherList[position]
                val intent = Intent(this@DisplayTeacherActivity, Start_FYP_Activityy::class.java)

                if(keyvalue=="head"){
                    val intent = Intent().apply {
                        intent.putExtra("keyvalue", "head")
                        intent.putExtra("Teacher_Object", selectedTeacher)
                        finish()
                    }
                }else{
                    val intent = Intent().apply {
                        intent.putExtra("keyvalue", "sec")
                        intent.putExtra("Teacher_Object", selectedTeacher)
                        finish()
                    }
                }
                startActivity(intent)
            }
        })
    }
}
