package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeTaskDetailFragment extends Fragment {

    private boolean initializedTimeField = false;

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
        Log.i("test", "initial = " + secondsInitial);
        if (secondsInitial > 0) {
            int hours = secondsInitial / 3600;
            // Dividable by 24 hours?
            if (hours % 24 == 0) {
                timeChoiceSpinner.setSelection(1, false);
                hours /= 24;
            }
            timeChoiceField.setText("" + hours);
        } else
            timeChoiceField.setText("" + 1);

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
                        Log.i("Test", "Time in seconds=" + getCastedActivity().getTaskDuration());
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
                if (!initializedTimeField) {
                    initializedTimeField = true;
                    return;
                }
                // Day(s) selected.
                if (id == 1) {
                    int hours = Integer.parseInt(timeChoiceField.getText().toString());
                    int days = hours / 24;
                    timeChoiceField.setText("" + (days > 0 ? days : 1));
                    // Hour(s) selected.
                    Log.i("Test", "converted to days");
                } else if (id == 0) {
                    int days = Integer.parseInt(timeChoiceField.getText().toString());
                    timeChoiceField.setText("" + (days * 24));
                    Log.i("Test", "converted to hours");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
