package technology.innovate.haziremployee.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import technology.innovate.haziremployee.R

private var progressDialog: Dialog? = null

fun Activity.setStatusBarTranslucent(makeTranslucent: Boolean) {
    if (makeTranslucent) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

}

fun Context.showToast(message: String?) {
    val toast: Toast = if (message == null || message.isEmpty()) {
        Toast.makeText(this, "Error Occurred", Toast.LENGTH_LONG)
    } else {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
    }
    try {
        toast.show()
    } catch (exception: Exception) {
        exception.printStackTrace()
        toast.show()
    }
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}


fun View.disable() {
    alpha = 0.5f
    isEnabled = false
}

fun View.enable() {
    alpha = 1.0f
    isEnabled = true
}

fun Context.showProgress() {
    try {
        if (progressDialog != null) {
            if (progressDialog?.isShowing!!) {
                progressDialog?.dismiss()
            }
        }
        progressDialog = Dialog(this)
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(R.layout.dialog_loading)
        progressDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    } catch (e: IllegalArgumentException) {

    }


}

fun Context.dismissProgress() {
    try {
        if (progressDialog != null) {
            if (progressDialog?.isShowing!!) {
                progressDialog?.dismiss()
            }
        }
    } catch (e: IllegalArgumentException) {

    }
}

fun Window.getWindowSizeAccordingToDevice() {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getRealMetrics(displayMetrics);
    val displayWidth = displayMetrics.widthPixels
    val displayHeight = displayMetrics.heightPixels
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(this.attributes)
    val dialogWindowWidth = (displayWidth * 0.8f).toInt()
    val dialogWindowHeight = WindowManager.LayoutParams.WRAP_CONTENT
    layoutParams.width = dialogWindowWidth
    layoutParams.height = dialogWindowHeight
    this.attributes = layoutParams
    this.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

fun <F : Fragment> AppCompatActivity.getFragment(fragmentClass: Class<F>): F? {
    val navHostFragment = this.supportFragmentManager.fragments.first() as NavHostFragment

    navHostFragment.childFragmentManager.fragments.forEach {
        if (fragmentClass.isAssignableFrom(it.javaClass)) {
            return it as F
        }
    }

    return null
}

fun getNavBuilder(): NavOptions.Builder {
    val st = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in)
        .setExitAnim(android.R.anim.fade_out)
        .setPopEnterAnim(android.R.anim.fade_in)
        .setPopExitAnim(android.R.anim.fade_out)
    st.apply {
        return this
    }
}

fun getUniqueID(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun RecyclerView.initRecyclerView(
    itemAnimator: RecyclerView.ItemAnimator,
    layoutManager: RecyclerView.LayoutManager
) {
    this.itemAnimator = itemAnimator
    this.layoutManager = layoutManager
}

fun Fragment?.runOnUiThread(action: () -> Unit) {
    this ?: return
    if (!isAdded) return
    activity?.runOnUiThread(action)
}

fun TextView.showKeyboard() {
    requestFocus()
    getInputMethodManager()?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun TextView.hideKeyboard() {
    clearFocus()
    getInputMethodManager()?.hideSoftInputFromWindow(windowToken, 0)
}

private fun TextView.getInputMethodManager() =
    ContextCompat.getSystemService(context, InputMethodManager::class.java)


