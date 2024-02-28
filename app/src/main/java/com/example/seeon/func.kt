package com.example.seeon

import android.content.Context
import android.content.Intent
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

fun Fragment.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(requireActivity(),activity::class.java)
    startActivity(intent)
    requireActivity().finish()
}

fun Fragment.replaceFragment(fragment: Fragment){
    this.parentFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(R.id.dataContainer,
            fragment
        ).commit()

}