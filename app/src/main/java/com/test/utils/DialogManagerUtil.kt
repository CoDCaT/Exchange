package com.test.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.test.R

object DialogManagerUtil {

    fun showConfirmExchangeDialog(context: Context, onConfirm: () -> Unit) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.exchangeConfirm))
            .setPositiveButton(R.string.exchangeOk) { _, _ -> onConfirm.invoke() }
            .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    fun showErrorTotalValueAlert(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.totalValueError))
            .setPositiveButton(R.string.ok) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    fun showErrorEqualsRateAlert(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.equalsRateError))
            .setPositiveButton(R.string.ok) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    fun showSuccessExchangeAlert(context: Context) {
        AlertDialog.Builder(context)
            .setMessage(context.getString(R.string.exchangeSuccess))
            .setPositiveButton(R.string.ok) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }
}