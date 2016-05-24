package de.eickemeyer.wake.on.lan.fragments;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.licences.Licenses;
import de.psdev.licensesdialog.LicensesDialogFragment;


public class AboutFragment extends BaseFragment {

    public static final String TAG = "TAG_AboutFragment";
    @BindView(R.id.about_acknowledgement)
    AppCompatTextView ackTextView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ackTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_licences)
    public void onLicenceButtonClick(View view) {
        final LicensesDialogFragment fragment = new LicensesDialogFragment.Builder(getActivity())
                .setNotices(new Licenses())
                .build();

        fragment.show(getFragmentManager(), null);
    }
}
