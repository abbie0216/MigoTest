package com.ab.migotest.model.db

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ab.migotest.model.db.dao.PassDao
import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.model.enums.PassType
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase() {

    private lateinit var db: AppDatabase
    private lateinit var passDao: PassDao

    private val fakeDayData = PassItem(
        id = 1,
        type = PassType.DAY,
        value = 1,
        price = 100,
    )
    private val fakeHourData1 = PassItem(
        id = 2,
        type = PassType.HOUR,
        value = 1,
        price = 10,
    )
    private val fakeHourData2 = PassItem(
        id = 3,
        type = PassType.HOUR,
        value = 2,
        price = 20,
    )

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        passDao = db.passDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPass() = runBlocking {
        db.withTransaction {
            passDao.insert(fakeDayData)
            val dayList = passDao.getAllByType(PassType.DAY)
            assertEquals(dayList.size, 1)
        }
    }

    @Test
    fun getAllPassByType() = runBlocking {
        passDao.insert(fakeDayData)
        passDao.insert(fakeHourData1)
        passDao.insert(fakeHourData2)
        val dayList = passDao.getAllByType(PassType.DAY)
        assertEquals(dayList.filter { passItem -> passItem.type == PassType.DAY }.size, 1)
        val hourList = passDao.getAllByType(PassType.HOUR)
        assertEquals(hourList.filter { passItem -> passItem.type == PassType.HOUR }.size, 2)
    }
}