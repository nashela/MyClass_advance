package com.shelazh.myclass.ui.login

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.openActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.ui.register.form.RegisterActivity
import com.shelazh.myclass.databinding.ActivityLoginBinding
import com.shelazh.myclass.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    var inputPhone = ""
    var inputPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        binding.activity = this

        binding.btnLogin.setOnClickListener {
            validateLogin()
        }

        binding.txtRegisterAcc.setOnClickListener{
            openActivity<RegisterActivity>()
            finish()
        }

        observe()

//        binding.btnLogin.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    viewModel.loginResponse.collect{
                        if (it.status == ApiStatus.LOADING){
                            loadingDialog.show(R.string.label_logging_in)
                        }else if (it.status == ApiStatus.SUCCESS){
                            loadingDialog.dismiss()
                            openActivity<HomeActivity>()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun validateLogin(){
        if (inputPhone.isEmpty()){
            binding.inputPhone.error = getString(R.string.error_phone_cannot_be_empty)
            return
        }
        if (inputPassword.isEmpty()){
            binding.inputPassword.error = getString(R.string.error_password_cannot_be_empty)
            return
        }
        viewModel.login(inputPhone, inputPassword)
    }
}