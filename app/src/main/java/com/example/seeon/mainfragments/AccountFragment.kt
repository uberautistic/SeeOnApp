package com.example.seeon.mainfragments

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.seeon.LoginActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentAccountBinding
import com.example.seeon.getFormattedUserPhoneNumber
import com.example.seeon.hideKeyboard
import com.example.seeon.models.User
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUserTable: DatabaseReference
    private lateinit var mUser: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        init()
        fillFields()
        return binding.root
    }


    private fun fillFields() {
        val userPhone=mAuth.currentUser?.phoneNumber
        var userName=""
        var userPhotoURL=""
        mUserTable.child(userPhone!!).get().addOnSuccessListener {
            if (it.exists()){
                userName=it.child("uname").value.toString()
                userPhotoURL=it.child("uphoto").value.toString()
                binding.userNameTV.setText(userName)
                binding.userPhoneNumberTV.setText(getFormattedUserPhoneNumber(userPhone))
                Picasso.get()
                    .load(userPhotoURL)
                    .placeholder(R.color.grey)
                    .into(binding.userPhotoIV)
                val userPaymentMethod=it.child("userPaymentMethod").value.toString()
                if (userPaymentMethod=="1"){
                    val mCardsTable= FirebaseDatabase.getInstance().getReference("paymentMethods")
                    mCardsTable.child(userPhone).get().addOnSuccessListener {
                        if (it.exists()){
                            val number= it.child("cardNumber").value.toString()
                            binding.cardInfo.visibility=View.VISIBLE
                            binding.addCardButton.visibility=View.GONE
                            binding.cardInfo.text="**** **** **** "+number.substring(15,19)
                        }
                    }
                }
            }
        }
    }
    class CreditCardmmYYtextWatcher : TextWatcher {
        private var current = ""

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString() != current) {
                val userInput = s.toString().replace(nonDigits,"")
                if (userInput.length <= 4) {
                    current = userInput.chunked(2).joinToString("/")
                    s.filters = arrayOfNulls<InputFilter>(0)
                }
                s.replace(0, s.length, current, 0, current.length)
            }
        }
    }



    class CreditCardNumberFormattingTextWatcher : TextWatcher {
        private var current = ""

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            if (s.toString() != current) {
                val userInput = s.toString().replace(nonDigits,"")
                if (userInput.length <= 16) {
                    current = userInput.chunked(4).joinToString(" ")
                    s.filters = arrayOfNulls<InputFilter>(0)
                }
                s.replace(0, s.length, current, 0, current.length)
            }
        }
    }
    companion object {
        private val nonDigits = Regex("[^\\d]")
    }
    private fun init() {
        binding.addCardButton.setOnClickListener {
            binding.addCardLayout.visibility=View.VISIBLE
        }
        binding.cardMMGGET.addTextChangedListener(CreditCardmmYYtextWatcher())
        binding.cardNumberET.addTextChangedListener(CreditCardNumberFormattingTextWatcher())
        binding.cardNumberET.addTextChangedListener {
            val input= binding.cardNumberET.text.toString()
            if (input.length==19)
                binding.cardMMGGET.requestFocus()
        }
        binding.cardMMGGET.addTextChangedListener {
            val input=binding.cardMMGGET.text.toString()
            if (input.length==5)
                binding.cardCVCET.requestFocus()
        }
        binding.cardCVCET.addTextChangedListener {
            val input=binding.cardCVCET.text.toString()
            if (input.length==3)
                hideKeyboard()
        }
        binding.addCardButtonn.setOnClickListener {
            val number =binding.cardNumberET.text.toString()
            val mmgg= binding.cardMMGGET.text.toString()
            val cvc=binding.cardCVCET.text.toString()
            if(number.isNotEmpty()&&mmgg.isNotEmpty()&&cvc.isNotEmpty()){
                if((number.length==19)&&(mmgg.length==5)&&(cvc.length==3)){
                    val update= mutableMapOf<String,Any>()
                    update["userPaymentMethod"]=1
                    FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(mAuth.currentUser?.phoneNumber!!)
                        .updateChildren(update)
                        .addOnSuccessListener {
                            binding.addCardButton.visibility=View.GONE
                            binding.cardInfo.visibility=View.VISIBLE
                            val lastDigits=number.substring(15,19)
                            binding.cardInfo.text="**** **** **** "+ lastDigits
                        }
                    val paymentMethodData= mutableMapOf<String,Any>()
                    paymentMethodData["cardNumber"]=number
                    paymentMethodData["mmYY"]=mmgg
                    paymentMethodData["cvc"]=cvc
                    FirebaseDatabase.getInstance().reference
                        .child("paymentMethods")
                        .child(mAuth.currentUser?.phoneNumber!!)
                        .updateChildren(paymentMethodData)
                        .addOnSuccessListener {
                        }
                    binding.addCardLayout.visibility=View.GONE
                } else
                    showToast(requireActivity(),"Введите данные корректно!")
            } else
                showToast(requireActivity(), "Заполните все поля!")

        }
        mUserTable= FirebaseDatabase.getInstance().getReference("users")
        mAuth= FirebaseAuth.getInstance()
        registerForContextMenu(binding.menuButton)
        binding.menuButton.setOnClickListener {
            requireActivity().openContextMenu(binding.menuButton)
        }
        val userPhone=mAuth.currentUser?.phoneNumber
        var uid: String
        var uname: String
        var ubirtdate: String
        var uphoto: String
        mUserTable.child(userPhone!!).get().addOnSuccessListener {
            if (it.exists()){
                uid= it.child("uid").value.toString()
                uname=it.child("uname").value.toString()
                uphoto=it.child("uphoto").value.toString()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_2, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.accountMenuItem->{
                showToast(requireActivity(),"account management")
                return true
            }
            R.id.notificationsMenuItem->{
                showToast(requireActivity(),"notifications")
                return true
            }
            R.id.cinemaMenuItem->{
                showToast(requireActivity(),"cinema")
                return true
            }
            R.id.savedMenuItem->{
                showToast(requireActivity(),"saved")
                return true
            }
            R.id.supportMenuItem->{
                showToast(requireActivity(),"support")
                return true
            }
            R.id.logoutMenuItem->{
                mAuth.signOut()
                replaceActivity(LoginActivity())
                return true
            }
            else -> return false
        }
    }

}