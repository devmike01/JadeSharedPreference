package devmike.jade.com.jadesharedpreference

import android.app.Instrumentation
import android.content.Context
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.robolectric.Robolectric

@RunWith(JUnit4::class)
class SettingsPreferenceTest {


    lateinit var testClass : TestClass

    lateinit var context: Context

    private var TEST_STR ="Testing this annotation"

    @Before
    fun init(){
        context = mock(Context::class.java)
    }

    @Test
    fun testWrite(){
        val testClas = TestClass(context)
        testClas.init();
        testClas.writeTest(TEST_STR)
    }

    @Test
    fun readString(){
        assertEquals(testClass.getString(), TEST_STR)
    }

}