package org.mobileProgramming.maintermproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface
import android.view.WindowManager;
import android.widget.ProgressBar;
import com.sehun.android_pet_community.R

/**
 * atrr 속성 필요함
 * res>values>styles>newDialog  필요함
 *
 */
class ProgressCircleDialog(context: Context?) :
    Dialog(context!!, R.style.NewDialog) {
    companion object {
        const val DEF_CANCELABLE = true
        const val DEF_CANNOT_CANCELABLE = false

        fun show(context: Context?, title: CharSequence?, message: CharSequence?, indeterminate: Boolean = DEF_CANCELABLE, cancelable: Boolean = DEF_CANCELABLE, cancelListener: DialogInterface.OnCancelListener? = null): ProgressCircleDialog {
            val dialog = ProgressCircleDialog(context)
            dialog.setTitle(title)
            dialog.setCancelable(cancelable)
            dialog.setOnCancelListener(cancelListener)
            /* The next line will add the ProgressBar to the dialog. */dialog.addContentView(
                ProgressBar(context),
                WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            )
            dialog.show()
            return dialog
        }
    }
}