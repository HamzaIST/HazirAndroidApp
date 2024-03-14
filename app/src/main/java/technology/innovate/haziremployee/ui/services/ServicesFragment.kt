package technology.innovate.haziremployee.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import technology.innovate.haziremployee.databinding.FragmentServicesBinding
import technology.innovate.haziremployee.utility.setStatusBarTranslucent

class ServicesFragment : Fragment() {

    private val viewModel by viewModels<ServicesViewModel>()
    private lateinit var viewBinding: FragmentServicesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        requireActivity().setStatusBarTranslucent(true)
        viewBinding = FragmentServicesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Banking"))
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Insurance"))


        val servicesFragmentAdapter = ServicesFragmentAdapter(
            childFragmentManager,
            lifecycle
        )

        servicesFragmentAdapter.addFragment(BankingFragment())
        servicesFragmentAdapter.addFragment(BankingFragment())
        viewBinding.viewPager.adapter = servicesFragmentAdapter

    }

    class ServicesFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        private val fragmentList: ArrayList<Fragment> = ArrayList()
        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getItemCount(): Int {
            return fragmentList.size
        }
    }


}