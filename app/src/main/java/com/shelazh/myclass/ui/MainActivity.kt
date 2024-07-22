package com.shelazh.myclass.ui


import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.crocodic.core.base.activity.NoViewModelActivity
import com.crocodic.core.extension.openActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.data.repository.UserRepository
import com.shelazh.myclass.ui.login.LoginActivity
import com.shelazh.myclass.databinding.ActivityMainBinding
import com.shelazh.myclass.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : NoViewModelActivity<ActivityMainBinding>(R.layout.activity_main) {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnLetsGet.setOnClickListener{
            lifecycleScope.launch {
                if (userRepository.checkLogin()){
                    openActivity<HomeActivity>()
                }else{
                    openActivity<LoginActivity>()
                }
            }
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }

    }
}