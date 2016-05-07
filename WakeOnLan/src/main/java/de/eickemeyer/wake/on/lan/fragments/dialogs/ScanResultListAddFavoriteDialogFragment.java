package de.eickemeyer.wake.on.lan.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.activities.MainActivity;
import de.eickemeyer.wake.on.lan.database.DatabaseManager;
import de.eickemeyer.wake.on.lan.entities.ScanResult;
import de.eickemeyer.wake.on.lan.fragments.ScanFragment;

public class ScanResultListAddFavoriteDialogFragment extends DialogFragment {

    public static final String KEY_LIST_POSITION = "KEY_LIST_POSITION";
    private int mListClickPosition = -1;
    private View mContextView;

    public static ScanResultListAddFavoriteDialogFragment newInstance(int listClickPosition) {
        final ScanResultListAddFavoriteDialogFragment scanResultListAddFavoriteDialogFragment = new ScanResultListAddFavoriteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LIST_POSITION, listClickPosition);
        scanResultListAddFavoriteDialogFragment.setArguments(bundle);
        return scanResultListAddFavoriteDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListClickPosition = getArguments().getInt(KEY_LIST_POSITION, -1);
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContextView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_favorite, null);
        final MainActivity activity = (MainActivity) getActivity();

        return new AlertDialog.Builder(getActivity(), R.style.HdrDialogStyle)
                .setTitle(R.string.chooseFavoriteName)
                .setView(mContextView, 60, 20, 60, 10)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        activity.hideKeyboard();
                    }
                }).create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final MainActivity activity = ((MainActivity) getActivity());
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText favoriteInputText = (EditText) mContextView.findViewById(R.id.edit_alert_fav);
                    String enteredFavoriteName = favoriteInputText.getText().toString();
                    final View fragmentContainer = getActivity().findViewById(R.id.fragmentContainer);

                    if (enteredFavoriteName.isEmpty()) {
                        Snackbar.make(fragmentContainer, R.string.emptyFavoriteName, Snackbar.LENGTH_LONG).show();
                    } else if (DatabaseManager.getInstance().isFavoriteNameAlreadyInUse(enteredFavoriteName)) {
                        Snackbar.make(fragmentContainer, R.string.favoriteNameAlreadyExists, Snackbar.LENGTH_LONG).show();
                    } else {
                        ScanFragment fr = (ScanFragment) getFragmentManager().findFragmentByTag(ScanFragment.TAG);
                        ScanResult scanResult = fr.getRecyclerViewAdapter().getItem(mListClickPosition);
                        scanResult.favName = enteredFavoriteName;
                        DatabaseManager.getInstance().storeFavorite(scanResult);
                        dialog.dismiss();
                        activity.hideKeyboard();
                    }
                }
            });
        }
    }
}