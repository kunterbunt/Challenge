package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeTaskDetailFragment extends Fragment {

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

        // Populate spinner.
        final Spinner timeChoiceSpinner = (Spinner) getView().findViewById(R.id.timeAfterPreviousSpinner);
        ArrayAdapter<CharSequence> timeChoiceAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.timeAfterPreviousChoices, android.R.layout.simple_spinner_item);
        timeChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeChoiceSpinner.setAdapter(timeChoiceAdapter);

        // Change timeAfterPrev when user changed the value.
        EditText timeChoiceField = (EditText) getView().findViewById(R.id.timeAfterPreviousField);
        timeChoiceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    try {
                        int time = Integer.parseInt(s.toString());
                        if (timeChoiceSpinner.getSelectedItemId() == 1)
                            time *= 24;
                        getCastedActivity().setTaskTimeAfterPrevious(time * 3600);
                    } catch (NumberFormatException ex) {
                        Log.e(getCastedActivity().LOG_TAG, ex.toString());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        // Time in seconds.
        int time = getCastedActivity().getTaskTimeAfterPrevious();
        if (time > 0) {
            // Time in hours.
            time /= 3600;
            // Dividable by 24 hours?
            if (time % 24 == 0) {
                timeChoiceSpinner.setSelection(1);
                time /= 24;
            }
            timeChoiceField.setText("" + getCastedActivity().getTaskTimeAfterPrevious());
        } else
            timeChoiceField.setText("" + 1);

        // Set description field watcher.
        EditText descriptionField = (EditText) getView().findViewById(R.id.description);
        descriptionField.setText(getCastedActivity().getTaskDescription());
        descriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getCastedActivity().setTaskDescription("" + s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
