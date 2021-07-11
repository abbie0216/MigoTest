package com.ab.migotest.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ab.migotest.databinding.DialogAddPassBinding
import com.ab.migotest.view.BaseDialogFragment

class SelectDialogFragment : BaseDialogFragment() {

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_DATA = "KEY_DATA"
        fun newInstance(
            title: String,
            data: ArrayList<String>
        ): SelectDialogFragment {
            val fragment = SelectDialogFragment()
            val args = Bundle()
            args.putSerializable(KEY_TITLE, title)
            args.putSerializable(KEY_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

    var dialogFuncListener: GeneralDialogFuncListener? = null

    private var data: ArrayList<String> = arrayListOf()

    lateinit var binding: DialogAddPassBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getSerializable(KEY_TITLE) as String
        if (data.size == 0) {
            data.addAll(arguments?.getStringArrayList(KEY_DATA) as ArrayList<String>)
            data.add(0, "")
            data.add("")
        }

        binding.tvSelectTitle.text = title
        binding.rvSelectDialog.also {
            it.setHasFixedSize(true)
            it.adapter = selectDialogAdapter
            it.addOnScrollListener(onScrollListener)
            LinearSnapHelper().attachToRecyclerView(it)
            it.scrollToPosition(getSelectItemIndex())
        }

        binding.tvSelectCancel.setOnClickListener {
            dismiss()
            dialogFuncListener?.onCancelClick?.invoke()
        }

        binding.tvSelectConfirm.setOnClickListener {
            dismiss()
            dialogFuncListener?.onConfirmClick?.invoke()
        }
    }

    override fun isFullLayout(): Boolean {
        return false
    }

    fun getSelectItemIndex(): Int {
        return selectDialogAdapter.currentPosition - 1
    }

    fun setSelectItemIndex(index: Int) {
        selectDialogAdapter.updateCurrentPosition(index + 1)
        selectDialogAdapter.notifyDataSetChanged()
    }

    private val selectDialogAdapter by lazy {
        SelectDialogAdapter(data)
    }

    private val onScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        val selectPosition = (binding.rvSelectDialog.layoutManager as LinearLayoutManager)
                            .findFirstCompletelyVisibleItemPosition() + 1
                        selectDialogAdapter.updateCurrentPosition(selectPosition)
                        selectDialogAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}