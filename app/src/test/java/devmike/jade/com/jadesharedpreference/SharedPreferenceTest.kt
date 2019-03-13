package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.binder.JadeSharedPreference
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

//@Config(manifest= Config.NONE)
//@RunWith(RobolectricTestRunner::class)
@RunWith(JUnit4::class)
class SharedPreferenceTest {

    lateinit var context: Context

   // lateinit var mainActivity: MainActivity
    @Before
    fun init(){
        //mainActivity = Robolectric.buildActivity(MainActivity::class.java).get()
       context = mock(Context::class.java)
        val jsp = JadeSharedPreference.apply(this, context)

    }

    @Test
    fun testread(){
    }


}