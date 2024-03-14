package technology.innovate.haziremployee.ui.services


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter



class ServicesFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = BankingFragment()
            }
            1 -> {
                fragment = BankingFragment()
            }
        }
        return fragment!!
    }

    override fun getItemCount(): Int {
        return 2
    }

}