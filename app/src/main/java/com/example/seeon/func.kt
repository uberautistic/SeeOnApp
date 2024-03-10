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

    fun Fragment.getFormattedUserPhoneNumber(phoneNumber: String): String {
    val group1 = listOf(2, 3, 4)
    val group2 = listOf(5, 6, 7)
    val group3 = listOf(8, 9)
    val group4 = listOf(10, 11)
    return "+7 (" + phoneNumber.slice(group1) + ") " +
            phoneNumber.slice(group2) + "-" + phoneNumber.slice(group3) +
            "-" + phoneNumber.slice(group4)
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

