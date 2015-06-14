package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.ViewPagerAdapter;
import sebastians.challenge.data.OnSwipeListener;

public class ChallengeDetailFragment extends Fragment {

    public ChallengeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        ViewPager imageSwiper = (ViewPager) view.findViewById(R.id.imageSwiper);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());

        imageSwiper.setAdapter(adapter);
        return view;
    }
}


