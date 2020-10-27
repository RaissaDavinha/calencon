package com.example.calencon.mechanics

import android.content.Intent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.calencon.R
import com.example.calencon.data.User
import com.example.calencon.presentation.base.BaseChoiceDialog
import com.example.calencon.presentation.home.HomeActivity
import com.example.calencon.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.drawer_item.view.*
import kotlinx.android.synthetic.main.view_drawer_header.view.*
import kotlinx.android.synthetic.main.view_drawer_header.view.separator
import org.koin.core.KoinComponent

class DrawerManager(private val activity: HomeActivity,
                    private val user: User) : KoinComponent {

    private var choiceDialog: BaseChoiceDialog? = null

    fun setupDrawer() {
        activity.apply {
            val drawerToggle =
                ActionBarDrawerToggle(this, drawer_layout, R.string.menu_open, R.string.menu_close)
            drawer_layout.addDrawerListener(drawerToggle)
            drawerToggle.syncState()

            toolbar.menu.setOnClickListener {
                drawer_layout.openDrawer(GravityCompat.START)
            }

            setupMenuItems()

            navigation.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_history -> {
//                        redirect(HistoricActivity.getStartIntent(context))
                    }
                    R.id.action_help -> {
                    }
                    R.id.action_feedback -> {
                    }
                    R.id.action_settings -> {
//                        redirect(SettingsActivity.getStartIntent(context))
                    }
                    R.id.action_logout -> showLogoutDialog()
                }
                menuItem.isChecked = false
                drawer_layout.closeDrawers()
                true
            }
            setHeader()
        }
    }

    private fun setHeader() {
        activity.apply {
            navigation.getHeaderView(0)?.let { header ->
                user.name.let { name ->
                    header.name.text = name
                }

                header.edit_profile.setOnClickListener {
//                    redirect(ProfileActivity.getStartIntent(baseContext))
                }

                user.url.let {
                    //todo put user image
                }
            }
        }
    }

    private fun redirect(intent: Intent) {
        activity.apply {
            startActivity(intent)
        }
    }

    private fun showLogoutDialog() {
        activity.apply {
            choiceDialog = BaseChoiceDialog(assetId = R.drawable.ic_login_background,
                title = getString(R.string.logout_title),
                message = getString(R.string.logout_message),
                positiveButton = getString(R.string.yes), enableCloseDialog = true,
                context = baseContext, listener = object : BaseChoiceDialog.DialogListener {
                    override fun onNegativeClickListener() {

                    }

                    override fun onPositiveClickListener() {
//                        userRepository.logout()
                        choiceDialog?.dismiss()
                        startActivity(LoginActivity.getStartIntent(baseContext).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })
                    }
                }).apply {
                setCancelable(false)
                show()
            }
        }
    }

    private fun setupMenuItems() {
        activity.apply {
            navigation.viewTreeObserver.addOnGlobalLayoutListener {
                navigation.menu.apply {
                    findItem(R.id.action_history)?.actionView?.let {
//                        it.icon.setImageResource(R.drawable.ic_clock)
                        it.title.text = getString(R.string.menu_history)
                        it.description.text = getString(R.string.menu_history_message)
                    }
                    findItem(R.id.action_help)?.actionView?.let {
//                        it.icon.setImageResource(R.drawable.ic_question)
                        it.title.text = it.context.getString(R.string.menu_help)
                        it.description.text = getString(R.string.menu_help_message)
                    }
                    findItem(R.id.action_feedback)?.actionView?.let {
//                        it.icon.setImageResource(R.drawable.ic_feedback)
                        it.title.text = it.context.getString(R.string.menu_feedback)
                        it.description.text = getString(R.string.menu_feedback_message)
                    }
                    findItem(R.id.action_settings)?.actionView?.let {
//                        it.icon.setImageResource(R.drawable.ic_settings)
                        it.title.text = it.context.getString(R.string.menu_settings)
                        it.description.text = getString(R.string.menu_settings_message)
                    }
                    findItem(R.id.action_logout)?.actionView?.let {
//                        it.icon.setImageResource(R.drawable.ic_logout)
                        it.title.text = it.context.getString(R.string.menu_logout)
                        it.description.visibility = View.GONE
                        it.separator.visibility = View.GONE
                    }
                }
            }
        }
    }
}