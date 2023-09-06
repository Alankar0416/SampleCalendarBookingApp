package com.alan0416.samplecalendarbookingapplication

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alan0416.samplecalendarbookingapplication.databinding.ActivityCalendarBookingBinding
import java.util.*

class CalendarBookingActivity : AppCompatActivity() {

    companion object{
        private const val DEFAULT_EVENT_DURATION : Long = 1000*60*15L
    }

    private lateinit var binding: ActivityCalendarBookingBinding
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    private val appointmentStartTime: Calendar = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalendarBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.time.setIs24HourView(true)

        binding.date.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            appointmentStartTime.set(Calendar.YEAR, year)
            appointmentStartTime.set(Calendar.MONTH, monthOfYear)
            appointmentStartTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            Toast.makeText(this@CalendarBookingActivity, "Time set to ${appointmentStartTime.time}", Toast.LENGTH_SHORT).show()
        }

        binding.time.setOnTimeChangedListener { view, hourOfDay, minute ->
            appointmentStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            appointmentStartTime.set(Calendar.MINUTE, minute)
            Toast.makeText(this@CalendarBookingActivity, "Time set to ${appointmentStartTime.time}", Toast.LENGTH_SHORT).show()
        }



        binding.submit.setOnClickListener {
            if (!validate()) {
                return@setOnClickListener
            }

            intent = Intent(Intent.ACTION_INSERT)
            intent.data = CalendarContract.Events.CONTENT_URI
            intent.putExtra(
                CalendarContract.Events.TITLE,
                "Appointment confirmed  for ${binding.name.text}"
            )
            intent.putExtra(
                CalendarContract.Events.EVENT_LOCATION,
                "Actual text of the business that would appear on the location"
            )

            intent.putExtra(
                CalendarContract.Events.DESCRIPTION,
                "Hello ${binding.name.text},\n\nContact: ${binding.mobileNumber.text} \n\nThis is a confirmation of your appointment at XyZ.\nPlease contact at <> for any queries"
            )

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, appointmentStartTime.timeInMillis)
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, (appointmentStartTime.time.time + DEFAULT_EVENT_DURATION))

            val email = binding.emailAddress.text.toString()
            intent.putExtra(Intent.EXTRA_EMAIL, "foo@gmail.com, foo.bar@gmail.com, $email")

            startActivity(intent)
        }

    }

    private fun validate(): Boolean {
        val emailAddress: String = binding.emailAddress.text.toString()
        if (!emailAddress.matches(emailRegex.toRegex())) {
            Toast.makeText(this, "Please enter a valid Email address", Toast.LENGTH_SHORT).show()
            return false
        }

        val mobileNumber: String = binding.mobileNumber.text.toString()
        if (mobileNumber.length < 10) {
            Toast.makeText(this, "Please enter a valid Phone number", Toast.LENGTH_SHORT).show()
            return false
        }

        val name: String = binding.name.text.toString()
        if (name.length < 2) {
            Toast.makeText(this, "Please enter the full Name", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}