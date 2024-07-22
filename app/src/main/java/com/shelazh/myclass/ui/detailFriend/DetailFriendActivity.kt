package com.shelazh.myclass.ui.detailFriend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.crocodic.core.extension.parcelable
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.data.SchoolModel
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.databinding.ActivityDetailFriendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFriendActivity : BaseActivity<ActivityDetailFriendBinding, DetailFriendViewModel>(R.layout.activity_detail_friend) {

    var id = 1
    var name = ""
    var phone = ""
    var school: SchoolModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        id = intent.getIntExtra("id", 1)
        name = intent.getStringExtra("name") ?: ""
        school = intent.getParcelableExtra("schoolId")
        phone = intent.getStringExtra("phone") ?: ""
//        val name = intent.getStringExtra("name")
//        binding.textView.text = name

        val data = intent.parcelable<User>(DATA)
        binding.user = data

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    companion object {
        const val DATA = "data"
    }
}