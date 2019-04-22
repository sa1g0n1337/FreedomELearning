package freedom.com.freedom_e_learning.speaking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import freedom.com.freedom_e_learning.model.User;
import freedom.com.freedom_e_learning.model.speaking.Speaking;


public class SpeakingFragmentAdapter extends FragmentPagerAdapter {
    public final static int FRAGMENT_COUNT = 2;
    private Speaking speaking;
    private User user;

    public SpeakingFragmentAdapter(FragmentManager fragmentManager, Speaking speaking, User user) {
        super(fragmentManager);
        this.speaking = speaking;
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                SpeakingFragment1 tab1 = new SpeakingFragment1();
                bundle.putString("Speaking_article", speaking.getQuestion());
                bundle.putInt("Speaking_topic", speaking.getTopic());
//                bundle.putString("User ID", user.getUid()); //Lấy UID truyền qua fragment1
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                SpeakingFragment2 tab2 = new SpeakingFragment2();
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
                return "Speaking";
            case 1:
                return "Comment";
            default:
                return "";
        }
    }
}
