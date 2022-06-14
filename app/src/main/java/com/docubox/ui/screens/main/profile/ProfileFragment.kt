package com.docubox.ui.screens.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.modes.local.User
import com.docubox.databinding.FragmentProfileBinding
import com.docubox.ui.screens.auth.AuthActivity
import com.docubox.util.extensions.navigate
import com.docubox.util.extensions.setupActionBar
import com.docubox.util.extensions.showAlertDialog
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    private var user: User ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initViews()
        initClickListeners()
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Profile",true,{
            findNavController().popBackStack()
        })
    }

    private fun initViews() = with(binding) {
        user = viewModel.getUser()
        user?.let {
            tvUserName.text = it.userName
            tvUserEmail.text = it.userEmail
        }
    }

    private fun initClickListeners() = with(binding) {
        logoutBtn.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                val dialogAction = requireContext().showAlertDialog("Logout","Are you sure you want to logout?","Logout","Cancel")
                if(dialogAction) {
                    viewModel.logoutUser()
                    requireActivity().navigate(AuthActivity::class.java,true)
                }
            }
        }
    }

}
