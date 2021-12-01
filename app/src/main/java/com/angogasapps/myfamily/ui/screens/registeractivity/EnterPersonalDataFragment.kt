package com.angogasapps.myfamily.ui.screens.registeractivity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.FragmentEnterPersonalDataBinding
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.RegisterUserFunks
import com.angogasapps.myfamily.network.interfaces.users.UserService
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import java.util.*
import javax.inject.Inject

class EnterPersonalDataFragment(val onOldUserSignIn: () -> Unit) : Fragment() {
    @Inject
    lateinit var userService: UserService
    private lateinit var binding: FragmentEnterPersonalDataBinding
    private var timeContainer: Calendar = Calendar.getInstance()
    private var mTimeMillisBirthday = 0L
    private var userPhotoUri = Uri.EMPTY
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentEnterPersonalDataBinding.inflate(layoutInflater, container, false)
        binding.numberPhoneText.text = getString(R.string.you_phone_number) + ": " + AUTH.currentUser!!.phoneNumber
        val onDateSetListener = OnDateSetListener { view1: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            timeContainer.set(Calendar.YEAR, year)
            timeContainer.set(Calendar.MONTH, monthOfYear)
            timeContainer.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }
        binding.selectBirthdayBtn.setOnClickListener { v: View? ->
            val datePickerDialog = DatePickerDialog(requireActivity(), onDateSetListener,
                    timeContainer.get(Calendar.YEAR), timeContainer.get(Calendar.MONTH), timeContainer.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
        binding.registerButton.setOnClickListener { v: View? ->
            val mUserName = binding.userNameEditText.text.toString().trim()
            if (mUserName.isEmpty()) {
                context?.let { Toasty.error(it, getString(R.string.enter_your_name)) }
                return@setOnClickListener
            }
            if (mTimeMillisBirthday == 0L) {
                context?.let { Toasty.error(it, getString(R.string.enter_birhday)) }
                return@setOnClickListener
            }


            RegisterUserFunks.registerNewUser(mUserName, mTimeMillisBirthday, userPhotoUri, onOldUserSignIn, onError = {
                if (it != null) {
                    println(it.stackTrace.toString())
                    Toasty.error(requireContext(), R.string.something_went_wrong).show()
                }
            })

        }
        binding.selectUserPhotoLayout.setOnClickListener { v: View? -> userPhoto }
        return binding.root
    }

    private fun setInitialDateTime() {
        mTimeMillisBirthday = timeContainer.timeInMillis
        binding.birthdayTextView.text = DateUtils.formatDateTime(activity,
                mTimeMillisBirthday, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
    }

    private val userPhoto: Unit
        private get() {
            CropImage.activity().setAspectRatio(1, 1)
                    .setRequestedSize(256, 256)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(requireActivity(), this)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            userPhotoUri = CropImage.getActivityResult(data).uri
            binding.userPhotoImageView.background = null
            binding.userPhotoImageView.setImageURI(userPhotoUri)
        } catch (e: Exception) {
            e.printStackTrace()
            Toasty.error(requireActivity().applicationContext, getString(R.string.unknown_error)).show()
        }
    }
}