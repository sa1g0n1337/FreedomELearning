package freedom.com.freedom_e_learning.writing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import freedom.com.freedom_e_learning.model.writing.Writing;

public class WritingFragmentAdapter extends FragmentPagerAdapter {
    public final static int FRAGMENT_COUNT = 2;
    private Writing writing;
//    private String mUserID = "";

    public WritingFragmentAdapter(FragmentManager fragmentManager, Writing writing) {
        super(fragmentManager);
        this.writing = writing;
//        this.mUserID = userID;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                WritingFragment1 tab1 = new WritingFragment1();
                bundle.putString("Reading_article", writing.getQuestion());
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                WritingFragment2 tab2 = new WritingFragment2();
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
                return "Writing";
            case 1:
                return "Answer";
            default:
                return "";

        }
    }
}
