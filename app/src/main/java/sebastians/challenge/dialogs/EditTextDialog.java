package sebastians.challenge.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by kunterbunt on 14.06.15.
 */
public abstract class EditTextDialog {

    public static final String LOG_TAG = "EditTextDialog";

    private EditText input;

    public EditTextDialog(Context context, @Nullable String title, String positiveMessage, @Nullable String neutralMessage,
                          @Nullable String negativeMessage, @Nullable String hint) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setMessage(title);
        input = new EditText(context);
        if (hint != null)
            input.setHint(hint);

        builder.setView(input);

        builder.setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveButtonClick(input);
            }
        });

        if (neutralMessage != null) {
            builder.setNeutralButton(neutralMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onNeutralButtonClick(input);
                }
            });
        }

        if (negativeMessage != null) {
            builder.setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onNegativeButtonClick(input);
                }
            });
        }

        builder.show();
    }

    public abstract void onPositiveButtonClick(EditText input);

    public void onNeutralButtonClick(EditText input) {
        throw new UnsupportedOperationException("onNeutralButtonClick needs to be overridden!");
    }

    public void onNegativeButtonClick(EditText input) {
        throw new UnsupportedOperationException("onNegativeButtonClick needs to be overridden!");
    }
}
