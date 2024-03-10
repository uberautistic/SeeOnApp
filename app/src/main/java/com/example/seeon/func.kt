package com.example.seeon

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(context: Context, msg : String){
    Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
}
 fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
     val intent = Intent(this, activity::class.java)
     startActivity(intent)
     this.finish()
 }
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}
fun AppCompatActivity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
fun Fragment.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(requireActivity(),activity::class.java)
    startActivity(intent)
    requireActivity().finish()
}

