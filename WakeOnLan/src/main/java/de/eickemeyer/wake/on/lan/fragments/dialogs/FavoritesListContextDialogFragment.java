package de.eickemeyer.wake.on.lan.fragments.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.activities.MainActivity;
import de.eickemeyer.wake.on.lan.adapter.ContextMenuListAdapter;
import de.eickemeyer.wake.on.lan.entities.DialogContextMenuItem;
import de.eickemeyer.wake.on.lan.fragments.FavoritesFragment;


public class FavoritesListContextDialogFragment extends DialogFragment {

    public static final String TAG = "TAG_FavoritesContextDialogFragment";
    public static final String KEY_MESSAGE_STRING = "KEY_MESSAGE_STRING";
    private int mMessageStringId = -1;

    public static FavoritesListContextDialogFragment newInstance(int messageStringId) {
        final FavoritesListContextDialogFragment scanDialogFragment = new FavoritesListContextDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE_STRING, messageStringId);
        scanDialogFragment.setArguments(bundle);
        return scanDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessageStringId = getArguments().getInt(KEY_MESSAGE_STRING, -1);
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        Resources resources = activity.getResources();
        ListView contextView = (ListView) activity.getLayoutInflater().inflate(R.layout.context_menu, null);
        List<DialogContextMenuItem> dialogContextMenuItems = Arrays.asList(
                new DialogContextMenuItem(resources.getString(R.string.wake_up), ContextCompat.getDrawable(activity, R.drawable.dialog_wake_icon)),
                new DialogContextMenuItem(resources.getString(R.string.delete), ContextCompat.getDrawable(activity, R.drawable.dialog_delete_icon)));

        contextView.setAdapter(new ContextMenuListAdapter(activity, R.layout.dialog_item_row, dialogContextMenuItems));
        contextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnContextItemClickListener fr = (FavoritesFragment) getFragmentManager().findFragmentByTag(FavoritesFragment.TAG);
                if (fr != null) fr.onContextItemClick(position);
            }
        });

        int paddingTop = (int) resources.getDimension(R.dimen.context_dialog_padding_top);
        int paddingBottom = (int) resources.getDimension(R.dimen.context_dialog_padding_bottom);
        int paddingLeftRight = (int) resources.getDimension(R.dimen.context_dialog_padding_left_right);
        return new AlertDialog.Builder(getActivity(), R.style.HdrDialogStyle)
                .setView(contextView, paddingLeftRight, paddingTop, paddingLeftRight, paddingBottom)
                .setMessage(resources.getString(mMessageStringId, ""))
                .show();
    }
}
