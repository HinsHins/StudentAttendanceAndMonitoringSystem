package com.example.firestoreinsetprototype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import com.example.firestoreinsetprototype.Constant.FirestoreCollectionPath
import com.example.firestoreinsetprototype.Model.*
import com.example.firestoreinsetprototype.Util.SpinnerUtil
import com.example.firestoreinsetprototype.Util.retrieveData
import com.example.firestoreinsetprototype.Util.retrieveDataWithMatch
import com.google.firebase.firestore.FirebaseFirestore

class MonitoringActivity : AppCompatActivity() {

    private val timetablePath = FirestoreCollectionPath.TIMETABLES_PATH
    private val attendancesPath = FirestoreCollectionPath.ATTENDANCES_PATH
    private val studentsPath = FirestoreCollectionPath.STUDENTS_PATH
    private val modulesPath = FirestoreCollectionPath.MODULES_PATH
    private var selectedModule: Module? = null
    private var selectedTimetable: Timetable? = null
    private val timetables = ArrayList<Timetable>()
//    private val attendances = ArrayList<Attendance>()
    private val allAttendance = ArrayList<ArrayList<Attendance>>()

    private val modules = ArrayList<Module>()

    private val fb = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        retrieveModules()


    }

    private fun retrieveModules() {
        val modulesCollection = fb.collection(modulesPath)
        modulesCollection.retrieveData(modules as ArrayList<Model>, Module::class.java) {
            var modules = modules as ArrayList<Module>
            val modulesString = (modules.map { it.name } as ArrayList<String>)
            val modulesSpinner: Spinner = findViewById(R.id.module_Spinner)
            val moduleAdapter = SpinnerUtil.setupSpinner(
                this,
                modulesSpinner,
                modulesString
            ) {
                selectedModule = modules[it]
                retrieveTimetables()
            }
            moduleAdapter.notifyDataSetChanged()
        }
    }

    private fun retrieveTimetables() {
        timetables.clear()
        val timetableCollection = fb.collection(timetablePath)
        timetableCollection.retrieveDataWithMatch(
            "moduleId",
            selectedModule?.id ?: "",
            timetables as ArrayList<Model>,
            Timetable::class.java
        ) {
            var timetables = timetables as ArrayList<Timetable>
            timetables.forEach {
//                retrieveAttendance(allAttendance,it.id)
            }
//            timetables.sortBy { it.week }
//            val timetablesString = (timetables.map { it.week } as ArrayList<String>)
////            val timetablesSpinner: Spinner = findViewById(R.id.timetable_Spinner)
//            val timetableAdapter = SpinnerUtil.setupSpinner(
//                this,
////                timetablesSpinner,
//                timetablesString
//            ) {
//                selectedTimetable = timetables[it]
//                retrieveAttendance()
//            }
//            timetableAdapter.notifyDataSetChanged()
        }
    }

    private fun retrieveAttendance(arrayList: ArrayList<Attendance>,timetableId :String) {
        arrayList.clear()
        val attendanceCollection = fb.collection(attendancesPath)
        attendanceCollection.retrieveDataWithMatch(
            "timetableId",
            timetableId,
            arrayList as ArrayList<Model>,
            Attendance::class.java
        ) {
            Log.d("attendanceList", "$arrayList")
            calculateAttendanceRate(arrayList)
        }
    }

    private fun calculateAttendanceRate(arrayList: ArrayList<Attendance>){
        val total = arrayList.count()
        val present = arrayList.filter {
            it.status == true
        }.count()
        val rate = (present.toDouble()/total.toDouble()) * 100
        Log.d("RateTotal", "$total ")
        Log.d("RatePresent", "$present ")
        Log.d("Rate", "$rate ")
    }


}