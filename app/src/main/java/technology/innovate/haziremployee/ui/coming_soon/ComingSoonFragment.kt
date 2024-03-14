package technology.innovate.haziremployee.ui.coming_soon

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import technology.innovate.haziremployee.base.BaseFragment
import technology.innovate.haziremployee.databinding.FragmentServicesBinding

class ComingSoonFragment : BaseFragment<FragmentServicesBinding,ComingSoonViewModel>() {

    override fun createViewModel(): ComingSoonViewModel {
        return ViewModelProvider(this).get(ComingSoonViewModel::class.java)
    }

    override fun createViewBinding(layoutInflater: LayoutInflater?): FragmentServicesBinding {
        return FragmentServicesBinding.inflate(layoutInflater!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}