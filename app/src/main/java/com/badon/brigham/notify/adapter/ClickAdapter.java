package com.badon.brigham.notify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ClickAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public abstract static class ClickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ClickAdapter.OnItemClickListener mListener;
        private ClickAdapter.OnItemLongClickListener mLongListener;

        public ClickViewHolder(View v) {
            super(v);
            v.setClickable(true);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public void attachToListener(ClickAdapter.OnItemClickListener listener) {
            mListener = listener;
        }

        public void attachToLongListener(ClickAdapter.OnItemLongClickListener listener) {
            mLongListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongListener != null) {
                mLongListener.onItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }

}
