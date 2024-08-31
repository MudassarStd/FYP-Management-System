//package com.android.cuifypmanagementsystem.admin.activities
//
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.android.cuifypmanagementsystem.BaseApplication
//import com.android.cuifypmanagementsystem.R
//import com.android.cuifypmanagementsystem.adapters.selectTeachersforFYPheadSEC
//import com.android.cuifypmanagementsystem.databinding.ActivityDisplayTeacherBinding
//import com.android.cuifypmanagementsystem.datamodels.Teacher
//import com.android.cuifypmanagementsystem.utils.Constants.ACTION_SELECT_FYP_HEAD
//import com.android.cuifypmanagementsystem.utils.LoadingProgress.hideProgressDialog
//import com.android.cuifypmanagementsystem.utils.LoadingProgress.showProgressDialog
//import com.android.cuifypmanagementsystem.utils.Result
//import com.android.cuifypmanagementsystem.viewmodel.GlobalSharedViewModel
//import com.android.cuifypmanagementsystem.viewmodel.TeacherViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//
//@AndroidEntryPoint
//class DisplayTeacherActivity : AppCompatActivity(),
//    selectTeachersforFYPheadSEC.OnTeacherTestListItemListener {
//    private lateinit var binding: ActivityDisplayTeacherBinding
//    private lateinit var teacherRecyclerView: RecyclerView
//    private val teacherViewModel : TeacherViewModel by viewModels()
//    private lateinit var selectionViewModel: GlobalSharedViewModel
//    private val selectTeacherAdapter: selectTeachersforFYPheadSEC by lazy {
//        selectTeachersforFYPheadSEC(this, emptyList())
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityDisplayTeacherBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // selection VM for persisting selected values (head & secretory)
//        selectionViewModel = (application as BaseApplication).getGlobalSharedViewModel()
//
//        // invoke interface
//        selectTeacherAdapter.setOnTeacherTestListItemListener(this)
//
//        setUpRecyclerView()
//        fetchTeachersFromCloud()
//
//
//    }
//
//    override fun onItemClick(teacher : Teacher) {
//        if(intent.action == ACTION_SELECT_FYP_HEAD)
//        {
//            selectionViewModel.setSelectedHead(teacher)
//            finish()
//        }
//        else {
//            selectionViewModel.setSelectedSecretory(teacher)
//            finish()
//        }
//    }
//
//    private fun fetchTeachersFromCloud(){
//
//        teacherViewModel.notFypHeadSecretaries.observe(this){result ->
//            when(result){
//                is Result.Success -> {
//                    val data = result.data
//                    hideProgressDialog()
//                    if (data.isEmpty())
//                    {
//                        toggleViews(false, true)
//                    }
//                    else{
//                        toggleViews(true, false)
//                        selectTeacherAdapter.updateTeachersList(result.data)
//                    }
//                }
//                is Result.Loading -> {
//                    showProgressDialog("Loading Data, please wait..", this)
//                }
//                is Result.Failure -> {
//                    hideProgressDialog()
//                    val errorMessage = result.exception.message ?: "An unknown error occurred"
//                    Toast.makeText(this, "Data fetch failed: $errorMessage", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun toggleViews(showRecyclerView: Boolean, showNoTeacherMessage: Boolean) {
//        binding.Teacherrecyclerview.visibility = if (showRecyclerView) View.VISIBLE else View.GONE
//        binding.tvNoTeacherFound.visibility = if (showNoTeacherMessage) View.VISIBLE else View.GONE
//    }
//
//    private fun setUpRecyclerView(){
//        teacherRecyclerView = binding.Teacherrecyclerview
//        teacherRecyclerView.adapter = selectTeacherAdapter
//        teacherRecyclerView.layoutManager = LinearLayoutManager(this)
//    }
//
//
//}
