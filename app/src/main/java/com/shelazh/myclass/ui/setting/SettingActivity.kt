package com.shelazh.myclass.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.openActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.data.repository.UserRepository
import com.shelazh.myclass.databinding.ActivitySettingBinding
import com.shelazh.myclass.ui.MainActivity
import com.shelazh.myclass.ui.home.HomeActivity
import com.shelazh.myclass.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : NoViewModelActivity<ActivitySettingBinding>(R.layout.activity_setting) {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout2)
                .setPositiveButton(R.string.yes) { _, _ ->
                    lifecycleScope.launch {
                        userRepository.deleteUser()
                        openActivity<LoginActivity>()
                        finishAffinity()
                    }
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}