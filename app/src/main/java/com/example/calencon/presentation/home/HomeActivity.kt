package com.example.calencon.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.calencon.R
import com.example.calencon.data.GROUP_KEY
import com.example.calencon.data.Group
import com.example.calencon.mechanics.now
import com.example.calencon.presentation.group.AddContactsActivity
import com.example.calencon.presentation.group.CreateGroupDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.register_activity.*
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    var createGroupDialog: CreateGroupDialog? = null
    private var selectedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        home_view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(home_view_pager)

        create_group_button.setOnClickListener {
            createGroup()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun createGroup() {
        createGroupDialog = CreateGroupDialog(this, listener = object: CreateGroupDialog.OnOptionClickListener {
            override fun onDoneClicked(groupName: String) {
                val uid = groupName + FirebaseAuth.getInstance().currentUser?.uid + now(TimeUnit.SECONDS).toString()
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
}