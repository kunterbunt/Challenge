package sebastians.challenge;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.ChallengeItemAdapter;
import sebastians.challenge.adapter.ListAdapter;
import sebastians.challenge.adapter.ViewPagerAdapter;
import sebastians.challenge.data.ChallengeItem;
import sebastians.challenge.data.ImagePath;

public class AddChallengeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int mSectionNumber;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddChallengeFragment newInstance(int sectionNumber) {
        AddChallengeFragment fragment = new AddChallengeFragment();
        fragment.mSectionNumber = sectionNumber;
        return fragment;
    }

    public AddChallengeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if (mSectionNumber == 0) {
            rootView = inflater.inflate(R.layout.fragment_add_challenge_overview, container, false);
            // Set subchallenge list view adapter.
            final List<ChallengeItem> challengeItemList = new ArrayList<>();
            challengeItemList.add(new ChallengeItem("title", 1, 1, 1, false, new ImagePath("path", 1), new ArrayList<ImagePath>()));
            ((ListView) rootView.findViewById(R.id.subchallenge_list)).setAdapter(new ChallengeItemAdapter(getActivity(), challengeItemList));
            // Set button listener to add subchallenge.
            ((ImageButton) rootView.findViewById(R.id.addSubchallengeButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            rootView = inflater.inflate(R.layout.fragment_add_challenge_detail, container, false);
            // Populate time spinner.
            Spinner timeAfterPreviousSpinner = (Spinner) rootView.findViewById(R.id.timeAfterPreviousSpinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.timeAfterPreviousChoices, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeAfterPreviousSpinner.setAdapter(adapter);
            // Populate image pager.
            ViewPager imageSwiper = (ViewPager) rootView.findViewById(R.id.imageSwiper);
            List<String> imagePaths = new ArrayList<>();
//            imagePaths.add(Uri.parse());
            imagePaths.add("drawable://" + R.drawable.rubicscube_up);
            imageSwiper.setAdapter(new ViewPagerAdapter(getActivity().getApplicationContext(), imagePaths));
        }
        return rootView;
    }

}
