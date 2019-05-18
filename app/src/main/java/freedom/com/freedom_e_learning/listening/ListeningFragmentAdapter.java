package freedom.com.freedom_e_learning.listening;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import freedom.com.freedom_e_learning.model.listening.Listening;

public class ListeningFragmentAdapter extends FragmentPagerAdapter {

    public final static int FRAGMENT_COUNT = 2;
    private Listening listening;

    public ListeningFragmentAdapter(FragmentManager fragmentManager, Listening listening) {
        super(fragmentManager);
        this.listening = listening;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();

//        Gửi argument tới fragment
        switch (position) {
            case 0:
                ListeningFragment1 tab1 = new ListeningFragment1();
                bundle.putSerializable("Topic", listening.getTopicId());
                bundle.putSerializable("Listening_questions", listening.getQuestions());
                bundle.putSerializable("Listening_audio", listening.getAudioURL());
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                ListeningFragment2 tab2 = new ListeningFragment2();
                bundle.putSerializable("Listening_transcript", listening.getTranscript());
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
                return "Listening";
            case 1:
                return "Transcript";
            default:
                return "";
        }
    }
}