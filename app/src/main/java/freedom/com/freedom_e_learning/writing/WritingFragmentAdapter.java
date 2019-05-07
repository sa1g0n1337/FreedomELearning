package freedom.com.freedom_e_learning.writing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import freedom.com.freedom_e_learning.model.writing.Writing;

public class WritingFragmentAdapter extends FragmentPagerAdapter {
    public final static int FRAGMENT_COUNT = 2;
    private Writing writing;
    private String userId = "";
    private int topic = 0;

    public WritingFragmentAdapter(FragmentManager fragmentManager, Writing writing, String userID, int topic) {
        super(fragmentManager);
        this.writing = writing;
        this.userId = userID;
        this.topic = topic;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                WritingFragment1 tab1 = new WritingFragment1();
                Bundle bundle1 = new Bundle();
                bundle1.putString("WRITING_QUESTION", writing.getQuestion());
                bundle1.putString("User ID", userId);
                bundle1.putInt("TOPIC", topic);
                tab1.setArguments(bundle1);
                return tab1;
            case 1:
                WritingFragment2 tab2 = new WritingFragment2();
                Bundle bundle2 = new Bundle();
                bundle2.putString("User ID", userId);
                bundle2.putInt("TOPIC", topic);
                tab2.setArguments(bundle2);
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
