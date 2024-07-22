package com.shelazh.myclass.ui.detailProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.parcelable
import com.shelazh.myclass.R
import com.shelazh.myclass.data.SchoolModel
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.ui.password.EditPasswordActivity
import com.shelazh.myclass.ui.editProfile.EditProfileActivity
import com.shelazh.myclass.databinding.ActivityDetailProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProfileActivity : NoViewModelActivity<ActivityDetailProfileBinding>(R.layout.activity_detail_profile) {
    var id = 1
    var name = ""
    var phone = ""
    var school: SchoolModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnEditPassword.setOnClickListener {
            val intent = Intent(this, EditPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.activity = this

        id = intent.getIntExtra("id", 1)
        name = intent.getStringExtra("name") ?: ""
        phone = intent.getStringExtra("phone") ?: ""
        school = intent.getParcelableExtra("schoolId")

        val data = intent.parcelable<User>(DATA)
        binding.user = data
    }
    companion object {
        const val DATA = "data"
    }

}