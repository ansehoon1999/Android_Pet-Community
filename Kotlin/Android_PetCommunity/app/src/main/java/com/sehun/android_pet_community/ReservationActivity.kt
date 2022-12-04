package com.sehun.android_pet_community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ManagerPossibleTime
import com.sehun.android_pet_community.databinding.ActivityClientManagerIntroductionBinding
import com.sehun.android_pet_community.databinding.ActivityReservationBinding
import java.text.SimpleDateFormat
import java.util.*

class ReservationActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {
    private  var mbinding : ActivityReservationBinding? = null
    private val binding get() = mbinding!!
    val managerPossibleTime = ManagerPossibleTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ManagerFragment1ReservationOkayBtn.setOnClickListener {
            pushReservationInfo()
        }

    }

    private fun pushReservationInfo() {
        val myUid = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()
        val days : List<Calendar> = binding.calendarView.selectedDates

        var result = ""
        for (i in 0 until days.size) {
            val calendar = days.get(i)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val week = SimpleDateFormat("EE").format(calendar.getTime())
            val day_full = year.toString() + "년 "+ (month+1).toString()  + "월 " + day.toString() + "일 " + week.toString() + "요일"
            managerPossibleTime.time.put(myUid + day_full, day_full)
            result += (day_full + "\n")
        }

        FirebaseFirestore.getInstance().collection("ManagerPossibleTime").document(myUid).set(managerPossibleTime)
        Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        if (result == "") {
            Toast.makeText(this, "try again please", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }

    }

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mbinding = null
        super.onDestroy()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        clearSelectionsMenuClick()
        when (checkedId) {
            R.id.rb_single -> binding.calendarView.selectionType = SelectionType.SINGLE
            R.id.rb_multiple -> binding.calendarView.selectionType = SelectionType.MULTIPLE
            R.id.rb_range -> binding.calendarView.selectionType = SelectionType.RANGE
        }
    }

    private fun clearSelectionsMenuClick() {
        binding.calendarView.clearSelections()
    }


}