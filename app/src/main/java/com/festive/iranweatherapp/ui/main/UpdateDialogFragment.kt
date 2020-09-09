package com.festive.iranweatherapp.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.festive.iranweatherapp.R
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_update_dialog.*

class UpdateDialogFragment : DialogFragment() {

    lateinit var onUpdateFragmentActionListener: OnUpdateFragmentActionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateFragmentAgreeButton.setOnClickListener {
            onUpdateFragmentActionListener.agree()
        }
        updateFragmentDisagreeButton.setOnClickListener {
            onUpdateFragmentActionListener.disAgree()
        }
    }


    interface OnUpdateFragmentActionListener {
        fun agree()
        fun disAgree()
    }
}