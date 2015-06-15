package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.ViewPagerAdapter;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.dialogs.ButtonDialog;
import sebastians.challenge.tools.PhotoManager;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeTaskDetailFragment extends Fragment {

    public static final String LOG_TAG = "TaskDetailFragment";
    public static final int SELECT_PHOTO = 123;
    private int previousSpinnerItemSelected;
    private ViewPagerAdapter viewPagerAdapter;
    private Uri currentImageUri;

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
        viewPagerAdapter = new ViewPagerAdapter(getActivity());
        Log.i("TEST", "No:" + getCastedActivity().getTaskImagePaths().size());
        for (ImagePath path : getCastedActivity().getTaskImagePaths())
            viewPagerAdapter.add(Uri.parse(path.getPath()));
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);

        // Set edit image button action.
        ImageButton addImageButton = (ImageButton) getView().findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ButtonDialog(getActivity(), null, "Camera", "Remove", "Gallery", null) {
                    @Override
                    public void onPositiveButtonClick() {
                        currentImageUri = PhotoManager.requestToTakeImage(getActivity());
                    }

                    @Override
                    public void onNeutralButtonClick() {
                        super.onNeutralButtonClick();
                    }

                    @Override
                    public void onNegativeButtonClick() {
                        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        choosePictureIntent.setType("image/*");
                        choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(choosePictureIntent, SELECT_PHOTO);
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
                    viewPagerAdapter.add(currentImageUri);
                    viewPagerAdapter.notifyDataSetChanged();
                    break;
                case SELECT_PHOTO:
                    Uri uri = data.getData();
                    getActivity().grantUriPermission(getActivity().getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    viewPagerAdapter.add(uri);
                    viewPagerAdapter.notifyDataSetChanged();
                    break;
            }
        } else
            Log.e(LOG_TAG, "Recieved intent with resultCode != RESULT_OK.");
        // Convert Uris to ImagePaths.
        List<Uri> imageUriList = viewPagerAdapter.getImageUris();
        List<ImagePath> imagePathList = new ArrayList<>(imageUriList.size());
        for (Uri uri : imageUriList) {
            String path = uri.toString();
            if (path == null)
                Log.e(LOG_TAG, "Image path from Uri conversion failed: " + uri.toString());
            else
                imagePathList.add(new ImagePath(path));
        }
        getCastedActivity().setTaskImagePaths(imagePathList);
    }
}
