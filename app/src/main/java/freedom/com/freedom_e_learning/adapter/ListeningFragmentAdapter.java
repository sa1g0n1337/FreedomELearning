package freedom.com.freedom_e_learning.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import freedom.com.freedom_e_learning.listening.ListeningFragment1;
import freedom.com.freedom_e_learning.listening.ListeningFragment2;
import freedom.com.freedom_e_learning.listening.ListeningFragment3;

public class ListeningFragmentAdapter extends FragmentPagerAdapter {

    public final static int FRAGMENT_COUNT = 3;
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
            case 2:
                ListeningFragment3 tab3 = new ListeningFragment3();
                return tab3;

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
                return "Quiz";
            case 2:
                return "Transcript";
            default:
                return "";
        }
    }
}