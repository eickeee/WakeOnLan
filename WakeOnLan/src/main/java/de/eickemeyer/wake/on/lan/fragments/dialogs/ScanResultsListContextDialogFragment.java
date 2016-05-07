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
import de.eickemeyer.wake.on.lan.fragments.ScanFragment;

public class ScanResultsListContextDialogFragment extends DialogFragment {

    public static final String KEY_MESSAGE_STRING = "KEY_MESSAGE_STRING";
    public static final String TAG = "TAG_ScanContextDialogFragment";
    private int mMessageStringId = -1;

    public static ScanResultsListContextDialogFragment newInstance(int messageStringId) {
        final ScanResultsListContextDialogFragment scanResultsListContextDialogFragment = new ScanResultsListContextDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MESSAGE_STRING, messageStringId);
        scanResultsListContextDialogFragment.setArguments(bundle);
        return scanResultsListContextDialogFragment;
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
        final ListView contextView = (ListView) activity.getLayoutInflater().inflate(R.layout.context_menu, null);
        List<DialogContextMenuItem> dialogContextMenuItems = Arrays.asList(
                new DialogContextMenuItem(resources.getString(R.string.wake_up), ContextCompat.getDrawable(activity, R.drawable.dialog_wake_icon)),
                new DialogContextMenuItem(resources.getString(R.string.add), ContextCompat.getDrawable(activity, R.drawable.dialog_favorites_icon)));
        contextView.setAdapter(new ContextMenuListAdapter(activity, R.layout.dialog_item_row, dialogContextMenuItems));
        contextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnContextItemClickListener fr = (ScanFragment) getFragmentManager().findFragmentByTag(ScanFragment.TAG);
                fr.onContextItemClick(position);
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