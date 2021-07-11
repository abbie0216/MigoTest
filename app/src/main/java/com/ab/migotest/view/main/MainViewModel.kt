package com.ab.migotest.view.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.ab.migotest.model.api.ApiRepository
import com.ab.migotest.model.api.ApiResult
import com.ab.migotest.model.db.AppDatabase
import com.ab.migotest.model.db.dao.PassDao
import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.model.enums.PassType
import com.ab.migotest.model.vo.PassListItem
import com.ab.migotest.model.vo.StatusItem
import com.ab.migotest.view.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.util.*

class MainViewModel @ViewModelInject constructor(
    private val apiRepository: ApiRepository,
    private val db: AppDatabase,
    private val passDao: PassDao,
) : BaseViewModel(apiRepository) {

    private val _status = MutableLiveData<ApiResult<StatusItem>>()
    val status: LiveData<ApiResult<StatusItem>> = _status

    private val _passListData = MutableLiveData<ArrayList<PassListItem>>()
    val passListData: LiveData<ArrayList<PassListItem>> = _passListData

    private val _passUpdated = MutableLiveData<Boolean>()
    val passUpdated: LiveData<Boolean> = _passUpdated

    private val allDayPassList = arrayListOf<PassListItem.Pass>()
    private val allHourPassList = arrayListOf<PassListItem.Pass>()
    private val dayPassList = arrayListOf<PassListItem.Pass>()
    private val hourPassList = arrayListOf<PassListItem.Pass>()

    fun fetchStatus(isWifi: Boolean) {
        viewModelScope.launch {
            flow {
                val resp = apiRepository.fetchStatus(isWifi)
                if (!resp.isSuccessful) throw HttpException(resp)
                emit(ApiResult.success(resp.body()))
            }
                .flowOn(Dispatchers.IO)
                .catch { e -> emit(ApiResult.error(e)) }
                .collect { _status.value = it }
        }
    }

    fun getAllPassListByType(type: PassType): ArrayList<PassListItem.Pass> {
        return when (type) {
            PassType.DAY -> {
                if (allDayPassList.isEmpty()) {
                    for (i in 1..10) {
                        allDayPassList.add(
                            PassListItem.Pass(
                                PassItem(
                                    id = i.toLong(),
                                    type = type,
                                    value = i,
                                    price = i * 100
                                )
                            )
                        )
                    }
                }
                allDayPassList
            }
            PassType.HOUR -> {
                if (allHourPassList.isEmpty()) {
                    for (i in 1..10) {
                        allHourPassList.add(
                            PassListItem.Pass(
                                PassItem(
                                    id = (i + 10).toLong(),
                                    type = type,
                                    value = i,
                                    price = i * 10
                                )
                            )
                        )
                    }
                }
                allHourPassList
            }
        }
    }

    fun fetchPassList() {
        viewModelScope.launch(Dispatchers.IO) {
            db.withTransaction {

                dayPassList.addAll(passDao.getAllByType(PassType.DAY).map { pass ->
                    PassListItem.Pass(pass)
                })
                hourPassList.addAll(passDao.getAllByType(PassType.HOUR).map { pass ->
                    PassListItem.Pass(pass)
                })

                _passListData.postValue(buildUiListData(dayPassList, hourPassList))
            }
        }
    }

    fun addPass(item: PassListItem.Pass) {
        viewModelScope.launch(Dispatchers.IO) {
            passDao.insert(item.content)

            when (item.content.type) {
                PassType.DAY -> {
                    if (dayPassList.find { it.content.id == item.content.id } == null) {
                        dayPassList.add(item)
                        val newDayList = dayPassList.sortedWith(compareBy { it.content.value })
                        dayPassList.clear()
                        dayPassList.addAll(newDayList)
                    }
                }
                PassType.HOUR -> {
                    if (hourPassList.find { it.content.id == item.content.id } == null) {
                        hourPassList.add(item)
                        val newHourList = hourPassList.sortedWith(compareBy { it.content.value })
                        hourPassList.clear()
                        hourPassList.addAll(newHourList)
                    }
                }
            }

            _passListData.postValue(buildUiListData(dayPassList, hourPassList))
        }
    }

    private fun buildUiListData(
        dayPassList: List<PassListItem>,
        hourPassList: List<PassListItem>
    ): ArrayList<PassListItem> {
        val list = arrayListOf<PassListItem>()
        if (dayPassList.isNotEmpty()) {
            list.add(PassListItem.Separator(PassType.DAY))
            list.addAll(dayPassList)
        }
        if (hourPassList.isNotEmpty()) {
            list.add(PassListItem.Separator(PassType.HOUR))
            list.addAll(hourPassList)
        }
        return list
    }

    fun activatePass(passItem: PassItem) {
        viewModelScope.launch(Dispatchers.IO) {
            passItem.isActivated = true
            passItem.activateAt = Date()
            passItem.expireAt =
                calculateExpireTime(passItem.activateAt, passItem.value, passItem.type)
            Timber.d("activatePass: $passItem")
            passDao.update(passItem)

            when (passItem.type) {
                PassType.DAY -> dayPassList.find { passItem.id == it.content.id }?.content =
                    passItem
                PassType.HOUR -> hourPassList.find { passItem.id == it.content.id }?.content =
                    passItem
            }
            _passUpdated.postValue(true)
        }
    }

    private fun calculateExpireTime(startTime: Date, addValue: Int, type: PassType): Date {
        return when (type) {
            PassType.DAY -> {
                val calendar = Calendar.getInstance()
                calendar.time = startTime
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                calendar.add(Calendar.DAY_OF_MONTH, addValue)
                calendar.time
            }
            PassType.HOUR -> {
                val calendar = Calendar.getInstance()
                calendar.time = startTime
                calendar.add(Calendar.HOUR_OF_DAY, addValue)
                calendar.time
            }
        }
    }
}