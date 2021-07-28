package net.golbarg.findimagedifferences.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import net.golbarg.findimagedifferences.R;

import org.jetbrains.annotations.NotNull;

public class DialogHelpMessage extends DialogFragment {

    public static String TAG = DialogHelpMessage.class.getName();

    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getContext().getString(R.string.help_text))
                .setPositiveButton("Ok", (dialog, which) -> {})
                .create();
    }
}
