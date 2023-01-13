package com.ddxz.best.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ddxz.best.R

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                    .setView(R.layout.dialog_loading)
                    .create().apply {
                        this.window?.apply {
                            setBackgroundDrawableResource(android.R.color.transparent)
                        }
                    }

    override fun dismiss() {
        if (isAdded) {
            super.dismiss()
        }
    }

    companion object {
        const val TAG = "LoadingDialogFragment"
    }
}