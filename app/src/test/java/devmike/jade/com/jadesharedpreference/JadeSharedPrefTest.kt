package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.ReadInt
import devmike.jade.com.binder.JadeSharedPreference
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
class JadeSharedPrefTest {

    @Mock
    lateinit var testClass: TestClass

    @Mock
    lateinit var context: Context


    var TEST_VALUE =209


    @Before
    public fun init(){
        testClass = TestClass()
        context = RuntimeEnvironment.application.baseContext
        testClass.init(context)
    }



    @Test
    fun testWrite(){
        testClass.writeTest(TEST_VALUE)
    }

    @Test
    fun testRead(){
        assertEquals(testClass.readTest(), 201)
    }
}