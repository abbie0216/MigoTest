package com.ab.migotest.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.migotest.R
import com.ab.migotest.databinding.DialogPassDetailBinding
import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.view.BaseDialogFragment
import java.text.SimpleDateFormat

class DetailDialogFragment : BaseDialogFragment() {

    companion object {
        private const val KEY_DATA = "KEY_DATA"
        fun newInstance(
            data: PassItem,
        ): DetailDialogFragment {
            val fragment = DetailDialogFragment()
            val args = Bundle()
            args.putSerializable(KEY_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding: DialogPassDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPassDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passItem = arguments?.getSerializable(KEY_DATA) as PassItem
        binding.tvStatus.text = "Pass Status: ${if (passItem.isActivated) "Activated" else "Unactivated"}"
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        binding.tvCreateTime.text = dateFormat.format(passItem.createAt)
        if (passItem.isActivated) {
            binding.tvActivateTimeTitle.visibility = View.VISIBLE
            binding.tvActivateTime.visibility = View.VISIBLE
            binding.tvExpireTimeTitle.visibility = View.VISIBLE
            binding.tvExpireTime.visibility = View.VISIBLE
            binding.tvActivateTime.text = dateFormat.format(passItem.activateAt)
            binding.tvExpireTime.text = dateFormat.format(passItem.expireAt)
        }
    }

    override fun isFullLayout(): Boolean {
        return false
    }
}