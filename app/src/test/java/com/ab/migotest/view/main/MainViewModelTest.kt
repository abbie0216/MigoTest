package com.ab.migotest.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ab.migotest.MainCoroutineRule
import com.ab.migotest.model.api.ApiRepository
import com.ab.migotest.model.api.ApiResult
import com.ab.migotest.model.db.AppDatabase
import com.ab.migotest.model.db.dao.PassDao
import com.ab.migotest.model.vo.StatusItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import retrofit2.Response

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @MockK
    lateinit var apiRepository: ApiRepository

    @MockK
    lateinit var db: AppDatabase

    @MockK
    lateinit var passDao: PassDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun fetchStatus() = runBlockingTest {
        val viewModel = MainViewModel(apiRepository, db, passDao)
        val resultData = StatusItem("ok",200)
        val response = Response.success(resultData)

        coEvery { apiRepository.fetchStatus(false) } returns(response)

        viewModel.fetchStatus(false)

        coVerify { apiRepository.fetchStatus(false) }

        assert(viewModel.status.value is ApiResult.Success)
        assertEquals(
            resultData,
            (viewModel.status.value as ApiResult.Success).result
        )
    }
}