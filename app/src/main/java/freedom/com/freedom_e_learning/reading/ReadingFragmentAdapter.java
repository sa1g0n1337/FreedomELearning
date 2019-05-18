package freedom.com.freedom_e_learning.reading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import freedom.com.freedom_e_learning.model.reading.Reading;

public class ReadingFragmentAdapter extends FragmentPagerAdapter {

    public final static int FRAGMENT_COUNT = 2;
    private Reading reading;
//    private String mUserID = "";

    public ReadingFragmentAdapter(FragmentManager fragmentManager, Reading reading) {
        super(fragmentManager);
        this.reading = reading;
//        this.mUserID = userID;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                ReadingFragment1 tab1 = new ReadingFragment1();
                bundle.putString("Reading_article", reading.getArticle());
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                ReadingFragment2 tab2 = new ReadingFragment2();
                bundle.putSerializable("Topic", reading.getTopicId());
                bundle.putSerializable("Reading_questions", reading.getQuestions());
                tab2.setArguments(bundle);
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
                return "Reading";
            case 1:
                return "Quiz";
            default:
                return "";

        }
    }
}