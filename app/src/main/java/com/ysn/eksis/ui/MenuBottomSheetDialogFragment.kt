package com.ysn.eksis.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import com.ysn.eksis.R
import org.greenrobot.eventbus.EventBus

class MenuBottomSheetDialogFragment : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val view = View.inflate(context, R.layout.bottom_sheet_dialog_fragment_menu, null)
        dialog?.setContentView(view)

        val layoutParams = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior as CoordinatorLayout.Behavior
        (behavior as BottomSheetBehavior).isHideable = false
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /* nothing to do in here */
            }

        })
    }

    override fun onDismiss(dialog: DialogInterface?) {
        val hashMap = HashMap<String, Any>()
        EventBus.getDefault().postSticky(hashMap)
        super.onDismiss(dialog)
    }

}