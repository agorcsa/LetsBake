package com.example.andreeagorcsa.bakingsecrets.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private ArrayList<Step> mStepList;
    private OnItemClickListener mListener;

    public StepAdapter(ArrayList<Step> stepsList, OnItemClickListener listener) {
        mStepList = stepsList;
        mListener = listener;
    }

    /**
     * creates a viewHolder by inflating the item_step layout
     *
     * @param parent
     * @param i
     * @return a viewHolder with steps
     */
    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false);
        StepViewHolder stepViewHolder = new StepViewHolder(v);
        return stepViewHolder;
    }

    /**
     * displays in the TextView the value from JSON
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step currentStep = mStepList.get(position);
        holder.mTextViewShortDescription.setText(currentStep.getShortDescription());
    }

    /**
     * @return the size of the stepslist,
     * so the number of steps objects
     */
    @Override
    public int getItemCount() {
        if (mStepList == null) {
            return 0;
        }
        return mStepList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<Step> stepList, int position);
    }

    /**
     * creates a ViewHolder
     */
    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_short_description)
        TextView mTextViewShortDescription;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(mStepList, position);
                        }
                    }
                }
            });
        }
    }
}
