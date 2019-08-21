package com.example.andreeagorcsa.bakingsecrets.adapter;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.model.Ingredient;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> mIngredientList;

    public IngredientAdapter(ArrayList<Ingredient> ingredientList) {
        mIngredientList = ingredientList;
    }

    /**
     * creates a viewHolder by inflating the item_ingredient layout
     *
     * @param parent
     * @param i
     * @return a viewHolder with ingredients
     */
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(v);
        return ingredientViewHolder;
    }

    /**
     * displays in each TextView the value from JSON
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient currentItem = mIngredientList.get(position);
        holder.mTextViewQuantity.setText(currentItem.getQuantity());
        holder.mTextViewMeasure.setText(currentItem.getMeasure());
        holder.mTextViewIngredient.setText(currentItem.getIngredient());
    }

    /**
     * @return the size of the ingredients list,
     * so the number of ingredients objects
     */
    @Override
    public int getItemCount() {
        if (mIngredientList == null) {
            return 0;
        }
        return mIngredientList.size();
    }

    /**
     * creates a ViewHolder
     */
    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view_ingredient_icon)
        ImageView mImageViewIngredient;
        @BindView(R.id.text_view_quantity)
        TextView mTextViewQuantity;
        @BindView(R.id.text_view_measure)
        TextView mTextViewMeasure;
        @BindView(R.id.text_view_ingredient)
        TextView mTextViewIngredient;
        @BindView(R.id.checkbox_ingredient)
        CheckBox mCheckboxIngredient;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
