package com.example.andreeagorcsa.bakingsecrets.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.model.Recipe;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<Recipe> mRecipeList;
    // copy of the mRecipeList used for the SearchView, containing all the items
    private ArrayList<Recipe> mRecipeListFull;
    private OnItemClickListener mListener;
    private Filter recipeFilter = new Filter() {
        // runs on the background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Recipe> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mRecipeListFull);
            } else {
                // takes our input, transforms it to String, turns it to lower case,
                // to avoid case sensitivity and removes the blank space before and after our input
                String filteredPattern = constraint.toString().toLowerCase().trim();
                // checks if the what recipe name contains the filter pattern
                for (Recipe item : mRecipeListFull) {
                    if (item.getRecipeName().toLowerCase().contains(filteredPattern)) {
                        // if yes, add the item to the list
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        // results will be published on the UI
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mRecipeList.clear();
            mRecipeList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    // constructor for the recipe adapter
    public RecipeAdapter(Context mContext, ArrayList<Recipe> mRecipeList) {
        this.mContext = mContext;
        this.mRecipeList = mRecipeList;
        mRecipeListFull = new ArrayList<>(mRecipeList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * inflates the item layout and creates a view holder
     *
     * @param parent
     * @param i
     * @return viewHolder
     */
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    /**
     * we use the position to get the currentItem from the array list,
     * which we will display in the corresponding view of the viewHolder
     *
     * @param recipeViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        Recipe currentItem = mRecipeList.get(position);
        String recipeName = currentItem.getRecipeName();
        String imageUrl = currentItem.getImage();

        // if the imageUrl doesn't contain any String, put the following placeholder images,
        // according to the name of the cake
        if (TextUtils.isEmpty(imageUrl)) {
            switch (recipeName) {
                case "Nutella Pie":
                    recipeViewHolder.mRecipeImage.setImageResource(R.drawable.nutellapie);
                    break;
                case "Brownies":
                    recipeViewHolder.mRecipeImage.setImageResource(R.drawable.brownie);
                    break;
                case "Yellow Cake":
                    recipeViewHolder.mRecipeImage.setImageResource(R.drawable.yellowcake);
                    break;
                case "Cheesecake":
                    recipeViewHolder.mRecipeImage.setImageResource(R.drawable.cheesecake);
                    break;
            }

            // otherwise load with Picasso the image from the imageUrl
        } else {
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .into(recipeViewHolder.mRecipeImage);
        }

        recipeViewHolder.mRecipeName.setText(recipeName);
    }

    /**
     * @return the size of the recipe list,
     * so the number of recipes objects
     */
    @Override
    public int getItemCount() {
        if (mRecipeList == null) {
            return 0;
        }
        return mRecipeList.size();
    }

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * creates a viewHolder
     */
    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view_recipe)
        ImageView mRecipeImage;
        @BindView(R.id.text_view_recipe)
        TextView mRecipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
