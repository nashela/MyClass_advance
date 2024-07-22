package com.shelazh.myclass.ui.login

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.base64encrypt
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.snacked
import com.crocodic.core.helper.DateTimeHelper
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.ui.register.form.RegisterActivity
import com.shelazh.myclass.databinding.ActivityLoginBinding
import com.shelazh.myclass.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    @Inject
    lateinit var session: CoreSession

    var inputPhone = ""
    var inputPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.checkLogin {
            if (it) {
                openActivity<HomeActivity>()
                finish()
            }
        }
//            else {
//                observe()
//                initToken()
//            }
//        }
        observe()
        initToken()
        binding.activity = this

        binding.txtRegisterAcc.setOnClickListener {
            openActivity<RegisterActivity>()
            finish()
        }

        binding.btnLogin.setOnClickListener {
            validateLogin()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loginResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show(R.string.label_logging_in)
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<HomeActivity>()
                                finish()
                            }

                            else -> {
                                loadingDialog.dismiss()
                                binding.root.snacked(
                                    it.message ?: getString(R.string.error_unknown)
                                )
                            }
                        }
                    }
                }
                launch {
                    viewModel.tokenResponse.observe(this@LoginActivity) { response ->
                        when (response.status) {
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                binding.btnLogin.isVisible = true
                            }

//                            ApiStatus.LOADING -> loadingDialog.show("Get Token...")
//                            else -> {
//                                loadingDialog.dismiss()
//                                binding.root.snacked(response.message ?: "...")
//                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun validateLogin() {
        if (inputPhone.isEmpty()) {
            binding.inputPhone.error = getString(R.string.error_phone_cannot_be_empty)
            return
        }
        if (inputPassword.isEmpty()) {
            binding.inputPassword.error = getString(R.string.error_password_cannot_be_empty)
            return
        }
        viewModel.login(inputPhone, inputPassword)
    }

    private fun initToken() {
        lifecycleScope.launch {
            loadingDialog.show("Get instance, wait for second...")
            val dateNow = DateTimeHelper().dateNow()
            val tokenInit = "$dateNow|rahasia"
            val tokenEncrypt = tokenInit.base64encrypt()

            session.setValue("token", tokenEncrypt)

            viewModel.getToken()
        }
    }
}
