package com.example.andreeagorcsa.bakingsecrets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.adapter.StepAdapter;
import com.example.andreeagorcsa.bakingsecrets.model.Recipe;
import com.example.andreeagorcsa.bakingsecrets.model.Step;
import com.example.andreeagorcsa.bakingsecrets.ui.MainActivity;
import com.example.andreeagorcsa.bakingsecrets.ui.VideoActivity;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements StepAdapter.OnItemClickListener {

    // constant used for sending the step object through parcel
    public static final String STEP_KEY = "step";
    // constant used for sending the step position through parcel
    public static final String STEP_POSITION_KEY = "step position";
    // constant used for sending the list of step objects through parcel
    public static final String STEP_LIST_KEY = "step list";
    @BindView(R.id.recycler_view_steps)
    RecyclerView mStepRecyclerView;
    @BindBool(R.bool.is_tablet)
    boolean mIsTablet;

    private Context mContext;
    private StepAdapter mStepAdapter;
    private RecyclerView.LayoutManager mStepLayoutManager;

    /**
     * creates a view containing the list of the steps objects,
     * displayed using the StepAdapter
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view containing the steps
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);
        mStepRecyclerView.setHasFixedSize(true);

        StyleableToast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.click_on_step), R.style.toast_style).show();

        mContext = view.getContext();
        mStepLayoutManager = new LinearLayoutManager(mContext);
        mStepRecyclerView.setLayoutManager(mStepLayoutManager);

        Recipe recipe = getActivity().getIntent().getParcelableExtra(MainActivity.RECIPE_KEY);

        List<Step> steps = recipe.getSteps();

        mStepAdapter = new StepAdapter((ArrayList<Step>) steps, this);

        mStepRecyclerView.setAdapter(mStepAdapter);

        return view;
    }

    /**
     * mandatory method of the OnItemClickListener interface declared in the StepAdapter,
     * while clicking on a step object,
     * the step object, the position of the clicked step object and the full list of step objects will be sent through parcel
     * via Intent to the next activity, VideoActivity
     *
     * @param stepList
     * @param position
     */
    @Override
    public void onItemClick(ArrayList<Step> stepList, int position) {
        Step step = stepList.get(position);
        Integer stepIndex = position;

        // tablet
        if (mIsTablet) {
            //replace the right pane with a new `VideoFragment`, passing the selected step through arguments
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEP_KEY, step);
            bundle.putInt(STEP_POSITION_KEY, stepIndex);
            bundle.putParcelableArrayList(STEP_LIST_KEY, stepList);
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.video_container, videoFragment);
            transaction.commit();

            // phone
        } else {
            Intent stepIntent = new Intent(getActivity().getBaseContext(), VideoActivity.class);
            stepIntent.putExtra(STEP_KEY, (Parcelable) step);
            stepIntent.putExtra(STEP_POSITION_KEY, stepIndex);
            stepIntent.putParcelableArrayListExtra(STEP_LIST_KEY, stepList);
            startActivity(stepIntent);
        }
    }
}


