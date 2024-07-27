package com.shelazh.myclass.ui.register.form

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.openActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.data.SchoolModel
import com.shelazh.myclass.databinding.ActivityRegisterBinding
import com.shelazh.myclass.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {

    var inputName = ""
    var inputSchool: SchoolModel? = null
    var inputPhone = ""
    var inputPassword = ""
    var inputPasswordConfirm = ""

    private val schools = listOf(
        SchoolModel(1, "SMA 99 BIZZARE"),
        SchoolModel(2, "SMKN 1 BIZZARE"),
        SchoolModel(3, "SMKN 2 BIZZARE"),
        SchoolModel(4, "SMKN 3 BIZZARE"),
        SchoolModel(5, "SMKN 4 BIZZARE"),
        SchoolModel(6, "SMKN 5 BIZZARE")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.txtLoginAcc.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

        setupSchoolSpinner()
    }

    private fun setupSchoolSpinner(){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, schools.map { it.schoolName })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.schoolSpinner.adapter = adapter

        binding.schoolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                inputSchool = schools[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                inputSchool = null
            }
        }
    }

    private fun observeResponse() {
        lifecycleScope.launch {
            viewModel.registerResponse.collect {
                when (it.status) {
                    ApiStatus.LOADING -> {
                        it.message?.let { msg -> loadingDialog.show(msg) }
                    }

                    ApiStatus.SUCCESS -> {
//                        loadingDialog.setResponse("Register Success")
                        android.os.Handler().postDelayed({
                            openActivity<LoginActivity>()
                            loadingDialog.dismiss()
                        }, 2000)

                    }

                    ApiStatus.ERROR -> {
                        loadingDialog.setResponse(it.message ?: "Error saat mendaftar atau tidak ada Internet")
                    }

                    else -> {
                        loadingDialog.setResponse(it.message ?: "Gagal untuk Register")
                        android.os.Handler().postDelayed({
                            loadingDialog.dismiss()
                        }, 5000)
                    }
                }
            }
        }
    }

    private fun register() {
        inputName = binding.inputName.editText?.text.toString()
        inputPhone = binding.inputPhone.editText?.text.toString()
        inputPassword = binding.inputPassword.editText?.text.toString()
        inputPasswordConfirm = binding.inputPasswordConfirm.editText?.text.toString()

        if (inputName.isEmpty()) {
            binding.inputName.error = getString(R.string.error_name_cannot_be_empty)
            return
        }
        if (inputSchool == null) {
            binding.inputSchool.error = getString(R.string.error_school_cannot_be_empty)
            return
        }
        if (inputPhone.isEmpty()) {
            binding.inputPhone.error = getString(R.string.error_phone_cannot_be_empty)
            return
        }
        if (inputPassword.isEmpty()) {
            binding.inputPassword.error = getString(R.string.error_password_cannot_be_empty)
            return
        }
        if (inputPasswordConfirm.isEmpty()) {
            binding.inputPasswordConfirm.error = getString(R.string.error_password_confirm_cannot_be_empty)
            return
        }
        viewModel.register(inputPhone, inputName, inputPassword, inputPasswordConfirm, inputSchool!!.id ?: 0)
        observeResponse()
    }
}
