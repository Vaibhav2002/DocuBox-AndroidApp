package com.docubox.ui.screens.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.databinding.FragmentRegisterBinding
import com.docubox.ui.screens.main.DocuBoxActivity
import com.docubox.util.extensions.*
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        collectUIState()
        collectUiEvents()
    }

    private fun collectUiEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            RegisterScreenEvents.NavigateToHomeScreen -> requireActivity().navigate(
                DocuBoxActivity::class.java,
                true
            )
            RegisterScreenEvents.NavigateToLoginScreen -> findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            is RegisterScreenEvents.ShowToast -> requireContext().showToast(it.message)
        }
    }

    private fun collectUIState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        // change progress bar visibility acc to isLoading
        with(binding) {
            emailTIL.error = it.emailError
            passwordTIL.error = it.passwordError
            usernameTIL.error = it.usernameError
            emailTIET.isEnabled = it.areTextFieldsEnabled
            passwordTIET.isEnabled = it.areTextFieldsEnabled
            usernameTIET.isEnabled = it.areTextFieldsEnabled
            registerBtn.isEnabled = it.isRegisterButtonEnabled
            progressLayout.visibleOrGone(it.isLoading)
        }
    }

    private fun initListeners() = with(binding) {
        emailTIET.listenAfterChange(viewModel::onEmailTextChange)
        passwordTIET.listenAfterChange(viewModel::onPasswordTextChange)
        usernameTIET.listenAfterChange(viewModel::onUsernameChange)
        registerBtn.singleClick(viewModel::onRegisterButtonPressed)
        goToLogin.singleClick(viewModel::onGoToLoginPress)
    }

    private fun initViews() = with(binding) {}
}
