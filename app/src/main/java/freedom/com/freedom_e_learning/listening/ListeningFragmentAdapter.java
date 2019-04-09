package freedom.com.freedom_e_learning.listening;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListeningFragmentAdapter extends FragmentPagerAdapter {

    public final static int FRAGMENT_COUNT = 2;
//    private String mUserID = "";

    public ListeningFragmentAdapter(FragmentManager fm) {
        super(fm);
//        this.mUserID = userID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListeningFragment1 tab1 = new ListeningFragment1();
                return tab1;
            case 1:
                ListeningFragment2 tab2 = new ListeningFragment2();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Listening";
            case 1:
                return "Transcript";
            default:
                return "";
        }
    }
}