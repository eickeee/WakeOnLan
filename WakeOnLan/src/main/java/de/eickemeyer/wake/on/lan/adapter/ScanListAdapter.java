package de.eickemeyer.wake.on.lan.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.databinding.ScanResultCardBinding;
import de.eickemeyer.wake.on.lan.entities.ScanResult;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ScanResultViewHolder> {

    private final List<ScanResult> mScanResults;

    private final OnClickListener mListener;
    private int lastPosition = -1;
    private Context mContext;

    public ScanListAdapter(List<ScanResult> scanResults, OnClickListener listener) {
        this.mScanResults = scanResults;
        this.mListener = listener;
    }

    @Override
    public ScanResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View statusContainer = inflater.inflate(R.layout.scan_result_card, parent, false);
        return new ScanResultViewHolder(statusContainer);
    }

    @Override
    public void onBindViewHolder(final ScanResultViewHolder holder, final int position) {
        ScanResult result = mScanResults.get(holder.getAdapterPosition());
        holder.bind(result);

        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onRecyclerViewItemClick(holder.getAdapterPosition());
            }
        });
    }

    public void add(ScanResult item) {
        add(item, getItemCount());
    }

    public void add(ScanResult item, int position) {
        position = position < 0 ? getItemCount() : position;
        mScanResults.add(position, item);
        notifyItemInserted(position);
    }

    public void removeAll() {
        for (int i = getItemCount(); i > 0; i--)
            remove(i - 1);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mScanResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ScanResult getItem(int position) {
        if (position >= 0 && position < getItemCount())
            return mScanResults.get(position);
        else
            return null;
    }

    @Override
    public int getItemCount() {
        return mScanResults.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public List<ScanResult> getScanResults() {
        return mScanResults;
    }

    public interface OnClickListener {
        void onRecyclerViewItemClick(int position);
    }

    static class ScanResultViewHolder extends RecyclerView.ViewHolder {
        private ScanResultCardBinding binding;

        public ScanResultViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(ScanResult scanResult) {
            binding.setScanResult(scanResult);
        }
    }
}
