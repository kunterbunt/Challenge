package sebastians.challenge;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.ViewPagerImageAdapter;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.dialogs.ButtonDialog;
import sebastians.challenge.tools.PhotoManager;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeTaskDetailFragment extends Fragment {

    public static final String LOG_TAG = "TaskDetailFragment";
    private int previousSpinnerItemSelected;
    private ViewPagerImageAdapter viewPagerImageAdapter;
    private ImagePath currentImagePath;

    public AddChallengeTaskDetailFragment() {
    }

    public AddChallengeTaskDetail getCastedActivity() {
        return (AddChallengeTaskDetail) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_challenge_task_detail, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set name field.
        EditText nameField = (EditText) getView().findViewById(R.id.name);
        nameField.setText(getCastedActivity().getTaskTitle());
        // Change title when user changed this field's text.
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getCastedActivity().setTaskTitle("" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set description field watcher.
        EditText descriptionField = (EditText) getView().findViewById(R.id.description);
        descriptionField.setText(getCastedActivity().getTaskDescription());
        descriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getCastedActivity().setTaskDescription("" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Populate spinner.
        final Spinner timeChoiceSpinner = (Spinner) getView().findViewById(R.id.timeAfterPreviousSpinner);
        ArrayAdapter<CharSequence> timeChoiceAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.timeAfterPreviousChoices, android.R.layout.simple_spinner_item);
        timeChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeChoiceSpinner.setAdapter(timeChoiceAdapter);

        // Time in seconds.
        final EditText timeChoiceField = (EditText) getView().findViewById(R.id.timeAfterPreviousField);
        int secondsInitial = getCastedActivity().getTaskDuration();
        if (secondsInitial > 0) {
            int hours = secondsInitial / 3600;
            // Dividable by 24 hours?
            if (hours % 24 == 0) {
                timeChoiceSpinner.setSelection(1, false);
                hours /= 24;
            } else
                timeChoiceSpinner.setSelection(0, false);
            timeChoiceField.setText("" + hours);
        } else
            timeChoiceField.setText("" + 1);
        previousSpinnerItemSelected = timeChoiceSpinner.getSelectedItemPosition();

        // Change timeAfterPrev when user changed the value.
        timeChoiceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    try {
                        int hours = Integer.parseInt(s.toString());
                        // If days was selected convert those to hours.
                        if (timeChoiceSpinner.getSelectedItemId() == 1)
                            hours *= 24;
                        // Convert to seconds.
                        getCastedActivity().setTaskDuration(hours * 3600);
                    } catch (NumberFormatException ex) {
                        Log.e(getCastedActivity().LOG_TAG, ex.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Convert to other time unit if spinner selection changes.
        timeChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Day(s) selected.
                if (id == 1 && previousSpinnerItemSelected != 1) {
                    int hours = Integer.parseInt(timeChoiceField.getText().toString());
                    int days = hours / 24;
                    timeChoiceField.setText("" + (days > 0 ? days : 1));
                // Hour(s) selected.
                } else if (id == 0 && previousSpinnerItemSelected != 0) {
                    int days = Integer.parseInt(timeChoiceField.getText().toString());
                    timeChoiceField.setText("" + (days * 24));
                }
                previousSpinnerItemSelected = timeChoiceSpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Populate ViewPager
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        viewPagerImageAdapter = new ViewPagerImageAdapter(getActivity());
        for (ImagePath path : getCastedActivity().getTaskImagePaths())
            viewPagerImageAdapter.add(path);
        viewPagerImageAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerImageAdapter);

        // Set up for zoom-in animation.
        viewPagerImageAdapter.setUpForZoomAnimation(getView());

        // Set edit image button action.
        ImageButton addImageButton = (ImageButton) getView().findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ButtonDialog(getActivity(), null, "Camera", "Remove", "Gallery", null) {
                    @Override
                    public void onPositiveButtonClick() {
                        currentImagePath = new ImagePath(PhotoManager.requestToTakePhoto(getActivity()));
                    }

                    @Override
                    public void onNeutralButtonClick() {
                        viewPagerImageAdapter.removeCurrentImage();
                        viewPagerImageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        PhotoManager.requestToPickPhotoFromGallery(getActivity());
                    }
                };
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PhotoManager.REQUEST_TAKE_PHOTO:
                    viewPagerImageAdapter.add(currentImagePath);
                    viewPagerImageAdapter.notifyDataSetChanged();
                    break;
                case PhotoManager.REQUEST_PICK_PHOTO:
                    Uri uri = data.getData();
                    ImagePath imagePath = PhotoManager.convertContentUriToImagePath(getActivity(), uri);
                    viewPagerImageAdapter.add(imagePath);
                    viewPagerImageAdapter.notifyDataSetChanged();
                    break;
            }
        } else
            Log.e(LOG_TAG, "Recieved intent with resultCode != RESULT_OK.");
        // Set changed image paths in activity.
        getCastedActivity().setTaskImagePaths(viewPagerImageAdapter.getImagePaths());
    }
}
