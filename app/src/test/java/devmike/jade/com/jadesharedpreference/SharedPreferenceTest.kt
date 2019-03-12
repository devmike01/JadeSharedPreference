package devmike.jade.com.jadesharedpreference

import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SharedPreferenceTest {

    lateinit var mainActivity: MainActivity

    @Before
    fun init(){
        mainActivity = Robolectric.buildActivity(MainActivity::class.java).get()
    }

    @Test
    fun testread(){
        assertNotEquals(mainActivity.mFloat, 0)
    }
}