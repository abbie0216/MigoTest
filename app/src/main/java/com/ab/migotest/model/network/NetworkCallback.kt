package com.ab.migotest.model.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import timber.log.Timber

class NetworkCallback(
    private val onAvailable: () -> Unit = { },
    private val onLosing: () -> Unit = { },
    private val onLost: () -> Unit = { },
    private val onCapabilitiesChanged: (Boolean) -> Unit = { }
) :
    ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.d("onAvailable")
        onAvailable()
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        super.onLosing(network, maxMsToLive)
        Timber.d("onLosing")
       onLosing()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Timber.d("onLost")
        onLost()
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Timber.d( "onCapabilitiesChanged")
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Timber.d("now using wifi...")
                    onCapabilitiesChanged(true)
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Timber.d("now using cellular network...")
                    onCapabilitiesChanged(false)
                }
                else -> {
                    Timber.d("now using others...")
                }
            }
        }
    }

}