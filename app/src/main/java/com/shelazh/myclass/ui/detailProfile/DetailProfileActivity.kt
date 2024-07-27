package com.shelazh.myclass.ui.detailProfile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.parcelable
import com.shelazh.myclass.R
import com.shelazh.myclass.data.SchoolModel
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.data.repository.UserRepository
import com.shelazh.myclass.ui.password.EditPasswordActivity
import com.shelazh.myclass.ui.editProfile.EditProfileActivity
import com.shelazh.myclass.databinding.ActivityDetailProfileBinding
import com.shelazh.myclass.ui.detailFriend.DetailFriendActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailProfileActivity : NoViewModelActivity<ActivityDetailProfileBinding>(R.layout.activity_detail_profile) {
    var id = 1
    var name = ""
    var phone = ""
    var user: User? = null
    var school: SchoolModel? = null

    @Inject
    lateinit var userRepository: UserRepository

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
        school = intent.getParcelableExtra("schoolName")

        val test = SchoolModel(4, "tidak bisa")
        val schoolName = findViewById<TextView>(R.id.txt_school_profile)
        schoolName.text = test.schoolName
//        schoolName.text = school?.schoolName ?: "no school data"

        val data = intent.parcelable<User>(DATA)
        binding.user = data
        binding.school = school
    }

//    private fun toDetail(data: User) {
//        openActivity<EditProfileActivity> {
//            putExtra(EditProfileActivity.DATA, data)
//            putExtra("id", data.id)
//            putExtra("name", data.name)
////            putExtra("school", schoolList.schoolName)
//            putExtra("phone", data.phone)
//        }
//    }

    companion object {
        const val DATA = "data"
    }

}