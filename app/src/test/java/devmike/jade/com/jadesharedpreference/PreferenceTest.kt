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
class PreferenceTest {


    lateinit var testClass : TestClass

    lateinit var context: Context

    private var TEST_STR ="Testing this annotation"

    @Before
    fun init(){
        context = mock(Context::class.java)
        val testClass = TestClass(context)
        testClass.writeTest(TEST_STR)
    }

    @Test
    fun readString(){
        val testClass = TestClass(context)
        assertEquals(testClass.getString(), TEST_STR)
    }

}