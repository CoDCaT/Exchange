package com.test.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
fun View.onClick(function: () -> Unit) {
    RxView
        .clicks(this)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { function.invoke() }
}

@SuppressLint("CheckResult")
fun View.onLongClick(function: () -> Unit) {
    RxView
        .longClicks(this)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { function.invoke() }
}

@SuppressLint("CheckResult")
fun EditText.onEdit(function: (String) -> Unit) {
    RxTextView.textChanges(this)
        .skipInitialValue()
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { function.invoke(it.toString()) }
}

fun EditText.safeActionDone(function: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        val isActionDone = actionId == EditorInfo.IME_ACTION_DONE
        if (actionId == EditorInfo.IME_ACTION_DONE) function.invoke()
        isActionDone
    }
}

fun ViewGroup.inflate(@LayoutRes layout: Int): View =
    LayoutInflater.from(context).inflate(layout, this, false)

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}
