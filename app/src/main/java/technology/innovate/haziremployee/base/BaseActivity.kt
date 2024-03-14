package technology.innovate.haziremployee.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<BINDING : ViewBinding, VM : BaseViewModel> :
    AppCompatActivity() {
    lateinit var viewModel: VM
    lateinit var binding: BINDING
    protected abstract fun createViewModel(): VM
    protected abstract fun createViewBinding(layoutInflater: LayoutInflater): BINDING
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = createViewBinding(LayoutInflater.from(this))
        setContentView(binding.root)
        viewModel = createViewModel()
    }
}
