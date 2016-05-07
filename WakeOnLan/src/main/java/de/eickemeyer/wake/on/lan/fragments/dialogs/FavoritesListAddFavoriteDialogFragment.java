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
import de.eickemeyer.wake.on.lan.fragments.FavoritesFragment;
import de.eickemeyer.wake.on.lan.network.NetworkAddressValidator;


public class FavoritesListAddFavoriteDialogFragment extends DialogFragment {

    private View mDialogView;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        mDialogView = activity.getLayoutInflater().inflate(R.layout.dialog_add_favorite_manually, null);
        return new AlertDialog.Builder(activity, R.style.HdrDialogStyle)
                .setView(mDialogView, 60, 20, 60, 10)
                .setTitle(R.string.addToFavorites)
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
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View fragmentContainer = getActivity().findViewById(R.id.fragmentContainer);

                    String enteredFavName = ((EditText) mDialogView.findViewById(R.id.edit_alert_fav)).getText().toString();
                    String enteredMac = ((EditText) mDialogView.findViewById(R.id.edit_alert_mac)).getText().toString();
                    String enteredBroadcast = ((EditText) mDialogView.findViewById(R.id.edit_alert_broad)).getText().toString();


                    if (enteredFavName.isEmpty()) {
                        Snackbar.make(fragmentContainer, R.string.emptyFavoriteName, Snackbar.LENGTH_LONG).show();
                    } else if (DatabaseManager.getInstance().isFavoriteNameAlreadyInUse(enteredFavName)) {
                        Snackbar.make(fragmentContainer, R.string.favoriteNameAlreadyExists, Snackbar.LENGTH_LONG).show();
                    } else if (NetworkAddressValidator.isInvalidMac(enteredMac)) {
                        Snackbar.make(fragmentContainer, R.string.invalidMac, Snackbar.LENGTH_LONG).show();
                    } else if (NetworkAddressValidator.isInvalidIp(enteredBroadcast)) {
                        Snackbar.make(fragmentContainer, R.string.invalidBroadcast, Snackbar.LENGTH_LONG).show();
                    } else {
                        ScanResult result = new ScanResult.ScanResultBuilder()
                                .setBroadcast(enteredBroadcast)
                                .setMac(enteredMac)
                                .setFavName(enteredFavName)
                                .setIcon(R.drawable.icon_pc)
                                .build();

                        DatabaseManager.getInstance().storeFavorite(result);
                        FavoritesFragment fr = (FavoritesFragment) getFragmentManager().findFragmentByTag(FavoritesFragment.TAG);
                        if (fr != null)
                            fr.getRecyclerViewAdapter().add(result);
                        //inform user
                        Snackbar.make(fragmentContainer, R.string.dataSaved, Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                        getMainActivity().hideKeyboard();
                    }
                }
            });
        }
    }
    private MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }
}
