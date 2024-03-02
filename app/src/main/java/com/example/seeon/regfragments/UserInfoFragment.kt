package com.example.seeon.regfragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.R
import com.example.seeon.databinding.FragmentUserInfoBinding
import com.example.seeon.showToast
import com.theartofdev.edmodo.cropper.CropImage
import java.text.SimpleDateFormat
import java.util.Calendar

class UserInfoFragment : Fragment() {
    private  lateinit var binding: FragmentUserInfoBinding
    private lateinit var uName: String
    private lateinit var uBirthDate: String
    private lateinit var stringPhotoUri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentUserInfoBinding.inflate(inflater, container,false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initListeners()
    }
    private fun pickDate(){
        val myCalendar= Calendar.getInstance()
        val datePicker= DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat="dd.MM.yyyy"
            val sdf= SimpleDateFormat(myFormat)
            binding.selectBirthDateB.text = sdf.format(myCalendar.time)
        }
        DatePickerDialog(requireActivity(),datePicker, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }
    private fun initListeners(){
        binding.returnB.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.selectPicButton.setOnClickListener{
            binding.progressBar.visibility=View.VISIBLE
            selectPhotoUser()
        }
        binding.nextStageB.setOnClickListener {
            uName=binding.userNameET.text.toString()
            uBirthDate=binding.selectBirthDateB.text.toString()
            if (uName.length<2||uBirthDate=="Дата рождения \uD83D\uDCC5"||uBirthDate.isEmpty())
                showToast(requireActivity(),"Заполните все поля корректно!")
            else
                findNavController().navigate(R.id.action_userInfoFragment_to_AuthFragment, getBundleC())
        }
        binding.selectBirthDateB.setOnClickListener {
            pickDate()
        }
    }

    private fun selectPhotoUser() {
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600,600)
            .start(requireActivity(),this)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            &&resultCode==Activity.RESULT_OK&&data!=null){
            val photoUri= CropImage.getActivityResult(data).uri
            binding.userPhotoView.setImageURI(photoUri)
            stringPhotoUri= photoUri.toString()
            binding.progressBar.visibility=View.INVISIBLE
        }
    }




    private fun getBundleC():Bundle{
        val bundle= Bundle()
        bundle.putString("uname",uName)
        bundle.putString("ubirthdate",uBirthDate)
        bundle.putString("uphoto", stringPhotoUri)
        return bundle
    }

}