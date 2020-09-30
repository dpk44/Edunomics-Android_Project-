package com.dpk.hammoq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dpk.hammoq.R
import kotlinx.android.synthetic.main.fragment_step.view.*

class StepFragment : Fragment(){

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_step,container,false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            view.actionButton.setOnClickListener {
                if(activity is MainActivity){
                    (activity as MainActivity).jumpToGallery()
                }
            }
        }
    }
