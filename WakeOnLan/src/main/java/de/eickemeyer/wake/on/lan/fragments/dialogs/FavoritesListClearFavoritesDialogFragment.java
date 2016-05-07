package de.eickemeyer.wake.on.lan.fragments.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.activities.MainActivity;
import de.eickemeyer.wake.on.lan.database.DatabaseManager;
import de.eickemeyer.wake.on.lan.fragments.FavoritesFragment;

public class FavoritesListClearFavoritesDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();

        return new AlertDialog.Builder(activity, R.style.HdrDialogStyle)
                .setTitle(activity.getString(R.string.information))
                .setMessage(activity.getString(R.string.clearFavoritesMessage))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseManager.getInstance().deleteAllFavorites();
                        FavoritesFragment fr = (FavoritesFragment) getFragmentManager().findFragmentByTag(FavoritesFragment.TAG);
                        if (fr != null)
                            fr.getRecyclerViewAdapter().removeAll();
                    }
                })
                .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
}
