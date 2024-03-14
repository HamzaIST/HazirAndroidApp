package technology.innovate.haziremployee.ui.applyjobform.filterjoblist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.leave_request.*
import technology.innovate.haziremployee.databinding.FragmentJoblistfilterBinding
import technology.innovate.haziremployee.rest.entity.*
import technology.innovate.haziremployee.ui.requests.*
import technology.innovate.haziremployee.utility.*
import java.util.*

@AndroidEntryPoint
class Joblistfilter : Fragment() {

    private lateinit var documentRequestBottomSheetDialog: BottomSheetDialog
    private lateinit var leaveBottomSheetDialog: BottomSheetDialog

    private val viewModel by viewModels<RequestsViewModel>()
    private lateinit var viewBinding: FragmentJoblistfilterBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().setStatusBarTranslucent(true)
        viewBinding = FragmentJoblistfilterBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    class RequestFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        val fragmentList: ArrayList<Fragment> = ArrayList()
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
