package com.docubox.ui.screens.main.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.local.models.User
import com.docubox.databinding.FragmentProfileBinding
import com.docubox.ui.screens.auth.AuthActivity
import com.docubox.util.Constants.REPORT_BUG_URL
import com.docubox.util.Constants.VIEW_SOURCE_CODE_URL
import com.docubox.util.extensions.navigate
import com.docubox.util.extensions.setupActionBar
import com.docubox.util.extensions.showAlertDialog
import com.docubox.util.extensions.singleClick
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    private var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initViews()
        initClickListeners()
    }


    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Profile", true, {
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
        btnLogout.singleClick(this@ProfileFragment::handleLogout)
        btnAbout.singleClick {
            findNavController().navigate(R.id.action_profileFragment_to_aboutUsBottomSheetFragment)
        }
        btnReportBug.singleClick { openIntent(REPORT_BUG_URL) }
        btnViewSourceCode.singleClick { openIntent(VIEW_SOURCE_CODE_URL) }

    }

    private fun handleLogout() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        requireContext().showAlertDialog(
            "Logout",
            "Are you sure you want to logout?",
            "Logout",
            "Cancel"
        ).also {
            if (it) {
                viewModel.logoutUser()
                requireActivity().navigate(AuthActivity::class.java, true)
            }
        }
    }

    private fun openIntent(url: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            startActivity(this)
        }
    }

}
