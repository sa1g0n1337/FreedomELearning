package freedom.com.freedom_e_learning.speaking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import freedom.com.freedom_e_learning.model.speaking.Speaking;


public class SpeakingFragmentAdapter extends FragmentPagerAdapter {
    public final static int FRAGMENT_COUNT = 1;
    private Speaking speaking;

    public SpeakingFragmentAdapter(FragmentManager fragmentManager, Speaking speaking) {
        super(fragmentManager);
        this.speaking = speaking;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                SpeakingFragment1 tab1 = new SpeakingFragment1();
                bundle.putString("Speaking_article", speaking.getQuestion());
                tab1.setArguments(bundle);
                return tab1;
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
                return "Speaking";
            default:
                return "";
        }
    }
}
