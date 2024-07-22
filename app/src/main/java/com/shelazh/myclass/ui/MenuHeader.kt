package com.shelazh.myclass.ui

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.activity.NoViewModelActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.databinding.HeaderMenuBinding
import com.shelazh.myclass.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuHeader : NoViewModelActivity<HeaderMenuBinding>(R.layout.header_menu) {
    private lateinit var viewModel: LoginViewModel
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding.access = this
    }
    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    user = v
                }
            }
        }
    }
}
//    private fun toDetail(data: User){
//        openActivity<DetailFriendActivity>{
//            putExtra(DetailFriendActivity.DATA, data)
//            putExtra("id", data.id)
//            putExtra("name", data.name)
//            putExtra("school", data.schoolId)
//            putExtra("phone", data.phone)
//
//        }
//    }
//}