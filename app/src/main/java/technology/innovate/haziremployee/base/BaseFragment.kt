package technology.innovate.haziremployee.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<BINDING : ViewBinding, VM : BaseViewModel> : Fragment() {
    lateinit var viewModel: VM
    lateinit var binding: BINDING
    protected abstract fun createViewModel(): VM
    protected abstract fun createViewBinding(layoutInflater: LayoutInflater?): BINDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = createViewBinding(LayoutInflater.from(activity))
        viewModel = createViewModel()

        return binding.root
    }


}
