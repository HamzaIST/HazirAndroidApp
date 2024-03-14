package technology.innovate.haziremployee.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import technology.innovate.haziremployee.R
import technology.innovate.haziremployee.config.Constants
import technology.innovate.haziremployee.databinding.FragmentNotificationsBinding
import technology.innovate.haziremployee.rest.entity.Notifications
import technology.innovate.haziremployee.ui.HomeActivity
import technology.innovate.haziremployee.utility.*
import technology.innovate.haziremployee.utils.CustomDialog
import java.util.*

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private lateinit var notificationAdapter: NotificationAdapter
    private val viewModel by viewModels<NotificationsViewModel>()
    private lateinit var viewBinding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        SessionManager.init(requireContext())
        initNotificationsRecyclerList()
        loadAllNotificationsFromRemote()
        return viewBinding.root
    }

    private fun initNotificationsRecyclerList() {
        viewBinding.recyclerView.initRecyclerView(
            DefaultItemAnimator(), LinearLayoutManager(context)
        )
        notificationAdapter = NotificationAdapter(requireContext(), listOf())
        viewBinding.recyclerView.adapter = notificationAdapter
    }


    private fun loadAllNotificationsFromRemote() {
        viewModel.notifications()
        viewModel.notifications.observe(viewLifecycleOwner, notificationsObserver)
    }

    private var notificationsObserver: Observer<DataState<Notifications>> =
        androidx.lifecycle.Observer {
            when (it) {
                is DataState.Loading -> {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.show()
                }
                is DataState.Success -> {
                    viewBinding.progressBar.hide()
                    validateNotificationsData(it.item)
                }
                is DataState.Error -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    requireContext().showToast(it.error.toString())
                }
                is DataState.TokenExpired -> {
                    viewBinding.recyclerView.hide()
                    viewBinding.progressBar.hide()
                    viewBinding.apply {
                        viewBinding.errorLayout.errorText.text = "No Data Found"
                        viewBinding.errorLayout.root.show()
                        viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                    }
                    CustomDialog(requireActivity()).showNonCancellableMessageDialog(message = getString(
                        R.string.tokenExpiredDesc
                    ),
                        object : CustomDialog.OnClickListener {
                            override fun okButtonClicked() {
                                (activity as? HomeActivity?)?.logoutUser()
                            }
                        })
                }
            }
        }

    private fun validateNotificationsData(response: Notifications) {
        if (activity != null && isAdded) {
            if (response.status == Constants.API_RESPONSE_CODE.OK) {
                if (response.notificationData != null && response.notificationData.isNotEmpty()) {
                    viewBinding.errorLayout.root.hide()
                    viewBinding.recyclerView.show()
                    notificationAdapter.addList(response.notificationData)
                    (activity as? HomeActivity)?.loadNotificationCountFromRemote()
                } else {
                    notificationAdapter.addList(Collections.emptyList())

                    viewBinding.errorLayout.errorText.text = "No Data Found"
                    viewBinding.errorLayout.root.show()
                    viewBinding.errorLayout.errorLottieAnimationView.playAnimation()
                }


            } else {
                CustomDialog(requireActivity()).showInformationDialog(response.message)
            }

        }
    }
}
