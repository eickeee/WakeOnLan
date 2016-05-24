package de.eickemeyer.wake.on.lan.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.activities.MainActivity;
import de.eickemeyer.wake.on.lan.adapter.ScanListAdapter;
import de.eickemeyer.wake.on.lan.database.DatabaseManager;
import de.eickemeyer.wake.on.lan.entities.ScanResult;
import de.eickemeyer.wake.on.lan.fragments.dialogs.FavoritesListAddFavoriteDialogFragment;
import de.eickemeyer.wake.on.lan.fragments.dialogs.FavoritesListClearFavoritesDialogFragment;
import de.eickemeyer.wake.on.lan.fragments.dialogs.FavoritesListContextDialogFragment;
import de.eickemeyer.wake.on.lan.fragments.dialogs.OnContextItemClickListener;
import de.eickemeyer.wake.on.lan.network.NetworkConnectivity;
import de.eickemeyer.wake.on.lan.network.WakeOnLanPackageSender;
import de.eickemeyer.wake.on.lan.ui.FabMenu;
import de.eickemeyer.wake.on.lan.ui.FabMenuListener;


public class FavoritesFragment extends BaseFragment implements ScanListAdapter.OnClickListener, FabMenuListener, OnContextItemClickListener {

    public static final String TAG = "TAG_FavoritesFragment";
    private MainActivity mActivity;
    private int mListClickPosition;
    private ScanListAdapter mRecyclerViewAdapter;
    private FabMenu mFabMenu;

    @BindView(R.id.recyclerFavorites)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);
        mActivity = (MainActivity) getActivity();

        setupFavoritesList();
        return view;
    }

    private void setupFavoritesList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerViewAdapter = new ScanListAdapter(DatabaseManager.getInstance().getAllFavorites(), this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mFabMenu.closeMenu();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFabMenu = FabMenu.createFabMenu(this);
    }

    @Override
    public void onStop() {
        super.onPause();
        mFabMenu.closeMenu();
        mFabMenu.setVisibility(View.GONE);
        //otherwise the fav fragment leaks
        mFabMenu.setFavoritesFragment(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        mListClickPosition = position;
        FavoritesListContextDialogFragment.newInstance(R.string.chooseAction).show(getFragmentManager(), FavoritesListContextDialogFragment.TAG);
    }

    @Override
    public void onContextItemClick(int position) {
        switch (position) {
            case 0:
                onContextItemWakeupFavorite();
                break;
            case 1:
                onContextItemSelectedDeleteFavorite();
                break;
        }
        final FragmentManager fragmentManager = getFragmentManager();
        final Fragment fr = fragmentManager.findFragmentByTag(FavoritesListContextDialogFragment.TAG);
        if (fr != null)
            fragmentManager.beginTransaction().remove(fr).commitAllowingStateLoss();
    }

    @Override
    public void addFavorite() {
        new FavoritesListAddFavoriteDialogFragment().show(getFragmentManager(), null);
    }

    @Override
    public void clearFavorites() {
        new FavoritesListClearFavoritesDialogFragment().show(getFragmentManager(), null);
    }

    public void onContextItemSelectedDeleteFavorite() {
        DatabaseManager.getInstance().deleteFavorite(mRecyclerViewAdapter.getItem(mListClickPosition));
        mRecyclerViewAdapter.remove(mListClickPosition);
    }

    public void onContextItemWakeupFavorite() {
        ScanResult scanResult = mRecyclerViewAdapter.getItem(mListClickPosition);
        if (NetworkConnectivity.isConnectedToWifi()) {
            WakeOnLanPackageSender.sendWakeOnLanPackage(mActivity, scanResult.mac, scanResult.broadcast);
        } else {
            final View fragmentContainer = getActivity().findViewById(R.id.fragmentContainer);
            Snackbar.make(fragmentContainer, R.string.wifiNotConnectedMessageWake, Snackbar.LENGTH_LONG).show();
        }
    }

    public ScanListAdapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }
}
