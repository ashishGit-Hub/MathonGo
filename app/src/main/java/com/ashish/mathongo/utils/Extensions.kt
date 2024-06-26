package com.ashish.mathongo.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

object Extensions {

    fun Fragment.toast(msg : String?){
        msg?.let {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }
}