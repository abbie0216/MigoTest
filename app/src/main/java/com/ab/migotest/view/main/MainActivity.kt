package com.ab.migotest.view.main

import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ab.migotest.R
import com.ab.migotest.databinding.ActivityMainBinding
import com.ab.migotest.model.api.ApiResult
import com.ab.migotest.model.enums.PassType
import com.ab.migotest.model.network.NetworkCallback
import com.ab.migotest.view.dialog.DetailDialogFragment
import com.ab.migotest.view.dialog.GeneralDialogFuncListener
import com.ab.migotest.view.dialog.SelectDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val passAdapter by lazy {
        PassAdapter(
            onItemClick = {
                DetailDialogFragment.newInstance(it)
                    .show(supportFragmentManager, DetailDialogFragment::class.simpleName)
            },
            onBtnClick = {
                viewModel.activatePass(it)
            }
        )
    }
    private var networkCallback: NetworkCallback? = null
    private var connectivityManager: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initNetworkSetting()

        binding.rvPass.adapter = passAdapter
        binding.btnAddDayPass.setOnClickListener {
            openSelector(PassType.DAY)
        }
        binding.btnAddHourPass.setOnClickListener {
            openSelector(PassType.HOUR)
        }

        viewModel.status.observe(this, {
            when (it) {
                is ApiResult.Success -> {
                    Timber.d("result: ${it.result}")
                    binding.tvStatus.text = Gson().toJson(it)
                }
                is ApiResult.Error -> {
                    Timber.d("result: ${it.throwable}")
                    binding.tvStatus.text = it.throwable.message
                }
                else -> {
                }
            }
        })
        viewModel.passListData.observe(this, {
            passAdapter.submitList(it)
        })
        viewModel.passUpdated.observe(this, {
            if (it) {
                passAdapter.notifyDataSetChanged()
            }
        })

        viewModel.fetchPassList()
    }

    override fun onResume() {
        super.onResume()
        networkCallback?.let { registerNetworkListener(it) }

    }

    override fun onPause() {
        super.onPause()
        networkCallback?.let { unregisterNetworkListener(it) }
    }

    private fun initNetworkSetting() {
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = NetworkCallback(
            onCapabilitiesChanged = {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.tvStatus.text = getString(R.string.text_status_loading)
                }
                viewModel.fetchStatus(it)
            })
    }

    private fun registerNetworkListener(callback: NetworkCallback) {
        val builder = NetworkRequest.Builder()
        val request = builder.build()
        connectivityManager?.registerNetworkCallback(request, callback)
    }

    private fun unregisterNetworkListener(callback: NetworkCallback) {
        connectivityManager?.unregisterNetworkCallback(callback)
    }

    private fun openSelector(
        type: PassType
    ) {
        val data = viewModel.getAllPassListByType(type)
        val dialog = SelectDialogFragment.newInstance(
            "ADD $type PASS",
            data.map {
                "${it.content.value} $type PASS"
            }.toMutableList() as ArrayList<String>,

            )
        dialog.dialogFuncListener = GeneralDialogFuncListener(
            onConfirmClick = {
                val selectedItem = data[dialog.getSelectItemIndex()]
                Timber.d("select: ${selectedItem.content}")
                viewModel.addPass(selectedItem)
            },
            onCancelClick = {

            }
        )
        dialog.setSelectItemIndex(0)
        dialog.show(supportFragmentManager, "PassSelectDialog")
    }

}