
import com.example.edu.ksu.crop.MainActivity;
import com.example.edu.ksu.crop.R;
import com.example.edu.ksu.crop.MainActivity.SoilFragment;
import com.example.edu.ksu.crop.MainActivity.WeatherFragment;

import android.app.Activity;
import android.app.Fragment;
import android.test.*;

public class ActivityTest extends AndroidTestCase{
	
	WeatherFragment mWeatherFragment;
	SoilFragment mSoilFragment;
    int x, y;
    MainActivity mMainActivity;
    Fragment frag;
    Activity act;
	protected void setUp() throws Exception {
        mWeatherFragment = new WeatherFragment();
        mSoilFragment = new SoilFragment();
        x = 5;
        y = 4;
		super.setUp();

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testAdd(){
    	assertEquals(9, mWeatherFragment.add(x, y));
    }
    
}