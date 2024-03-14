package technology.innovate.haziremployee.utility
import android.R
import android.app.Activity
import android.content.Context
import androidx.annotation.IntDef
import androidx.appcompat.app.AlertDialog
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class CustomDialog(
    /**
     * Context of calling activity
     */
    private val context: Context
) {
    /**
     * Shows A Cancellable Error Dialog
     */
    fun showErrorDialog(errorMessage: String?) {
        showAlertBox(context, errorMessage, ERROR_MESSAGE, true, null)
    }

    fun showDecisionButtonDialog(
        messaage: String?,positiveButton:String?, negatioveButton:String? = null,
        cancellable: Boolean,
        userActionCLickListener: onUserActionCLickListener?
    ) {
        showUserActionDialog(context, messaage, positiveButton,negatioveButton,cancellable, userActionCLickListener)
    }

    fun showWarningDialog(errorMessage: String?) {
        showAlertBox(context, errorMessage, WARNING_MESSAGE, true, null)
    }

    fun showNonCancellableErrorDialog(errorMessage: String?) {
        showAlertBox(context, errorMessage, ERROR_MESSAGE, false, null)
    }

    fun showSuccessDialog(message: String?) {
        showAlertBox(context, message, SUCCESS_MESSAGE, true, null)
    }

    fun showNonCancellableSuccessDialog(message: String?) {
        showAlertBox(context, message, SUCCESS_MESSAGE, false, null)
    }

    fun showInformationDialog(message: String?) {
        showAlertBox(context, message, INFORMATION_MESSAGE, true, null)
    }

    fun showMessageDialogWithButton(message: String?, listener: OnClickListener?) {
        showAlertBox(context, message, INFORMATION_MESSAGE, true, listener)
    }

    fun showNonCancellableMessageDialog(message: String?, listener: OnClickListener?) {
        showAlertBox(context, message, INFORMATION_MESSAGE, false, listener)
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(ERROR_MESSAGE, INFORMATION_MESSAGE, SUCCESS_MESSAGE, WARNING_MESSAGE)
    annotation class MessageType
    interface OnClickListener {
        fun okButtonClicked()
    }

    interface onUserActionCLickListener {
        fun negativeButtonClicked()
        fun positiveButtonClicked()
    }

    companion object {
        /**
         * Constant for error message
         */
        const val ERROR_MESSAGE = 0

        /**
         * Constant for information message
         */
        const val INFORMATION_MESSAGE = 1

        /**
         * Constant for success message
         */
        const val SUCCESS_MESSAGE = 2
        const val WARNING_MESSAGE = 3
        fun showAlertBox(activity: Context?, message: String?, @MessageType messageType: Int,
                         cancellable: Boolean, okButtonClickListener: OnClickListener?
        ) {

            //If activity is not shown or Died else continue
            if (!isActive(activity as Activity?)) {
                return
            }
            val alertDialog = AlertDialog.Builder(activity!!).create()
            alertDialog.setMessage(message)
            when (messageType) {
                ERROR_MESSAGE -> {
                    alertDialog.setIcon(R.drawable.stat_notify_error)
                    alertDialog.setTitle("Error")
                }
                INFORMATION_MESSAGE -> {
                    alertDialog.setIcon(R.drawable.ic_dialog_info)
                    alertDialog.setTitle("Information")
                }
                SUCCESS_MESSAGE -> {
                    alertDialog.setIcon(R.drawable.checkbox_on_background)
                    alertDialog.setTitle("Success")
                }
                WARNING_MESSAGE -> {
                    alertDialog.setIcon(R.drawable.stat_sys_warning)
                    alertDialog.setTitle("Warning")
                }
            }
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, which ->
                okButtonClickListener?.okButtonClicked()
                alertDialog.cancel()
            }
            alertDialog.setCancelable(cancellable)
            alertDialog.show()
        }

        fun showUserActionDialog(
            activity: Context?, message: String?,positiveButton:String?,negatioveButton:String? = null,
            cancellable: Boolean, onUserActionCLickListener: onUserActionCLickListener?
        ) {
            //If activity is not shown or Died else continue
            if (!isActive(activity as Activity?)) {
                return
            }
            val alertDialog = AlertDialog.Builder(
                activity!!
            ).create()
            alertDialog.setMessage(message)
            alertDialog.setTitle("Confirmation")
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, positiveButton
            ) { dialog, which ->
                onUserActionCLickListener?.positiveButtonClicked()
                alertDialog.cancel()
            }
            if (!negatioveButton.isNullOrEmpty()) {
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, negatioveButton
                ) { dialogInterface, i ->
                    onUserActionCLickListener?.negativeButtonClicked()
                    alertDialog.cancel()
                }
            }
            alertDialog.setCancelable(cancellable)
            alertDialog.show()
        }

        fun isActive(activity: Activity?): Boolean {
            return if (activity != null) {
                try {
                    activity.window.decorView.isShown
                } catch (e: Exception) {
                    false
                }
            } else false
        }
    }
}