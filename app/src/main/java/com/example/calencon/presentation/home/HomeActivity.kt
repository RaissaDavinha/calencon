package com.example.calencon.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.calencon.R
import com.example.calencon.data.GROUP_KEY
import com.example.calencon.data.Group
import com.example.calencon.mechanics.now
import com.example.calencon.presentation.group.AddContactsActivity
import com.example.calencon.presentation.group.CreateGroupDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.register_activity.*
import me.everything.providers.android.calendar.CalendarProvider
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {
    var createGroupDialog: CreateGroupDialog? = null
    private var selectedUri: Uri? = null
    lateinit var calendarProvider: CalendarProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        home_view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(home_view_pager)

        create_group_button.setOnClickListener {
            createGroup()
        }

        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALENDAR), 1)
        }

        calendarProvider = CalendarProvider(baseContext)
        calendarProvider.calendars.list
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun createGroup() {
        createGroupDialog = CreateGroupDialog(
            this,
            listener = object : CreateGroupDialog.OnOptionClickListener {
                override fun onDoneClicked(groupName: String) {
                    val uid =
                        groupName + FirebaseAuth.getInstance().currentUser?.uid + now(TimeUnit.SECONDS).toString()
                    val newGroup = Group(uid, groupName, selectedUri, null)
                    val intent = Intent(baseContext, AddContactsActivity::class.java)
                    intent.putExtra(GROUP_KEY, newGroup)
                    startActivity(intent)
                    createGroupDialog?.dismiss()
                }

                override fun onCloseClicked() {
                    createGroupDialog?.dismiss()
                }

                override fun onSelectPhoto() {
                    selectPhoto()
                }
            })
        createGroupDialog?.show()
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 20)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20) {
            selectedUri = data?.data
            selectedUri?.let{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                photo_img.setImageBitmap(bitmap)
                select_photo_button.visibility = View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }
}