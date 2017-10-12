package io.dianto.baking.app.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.R;
import io.dianto.baking.app.model.Steps;
import io.dianto.baking.app.view.callback.RecipeStep_OnClickListener;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private static int clickedPosition;
    private List<Steps> mStepses;
    private RecipeStep_OnClickListener mCallback;

    public RecipeStepsAdapter(RecipeStep_OnClickListener callback) {
        this.mStepses = new ArrayList<>();
        this.mCallback = callback;
        clickedPosition = -1;
    }

    @Override
    public RecipeStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recipe_steps, parent, false);
        return new RecipeStepsViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecipeStepsViewHolder holder, int position) {
        holder.recipeStepTitle.setText(mStepses.get(holder.getAdapterPosition()).getShortDescription());

        final Context context = holder.itemView.getContext();
        if (clickedPosition == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mStepses.size();
    }

    public void setDataAdapter(List<Steps> stepses) {
        mStepses.clear();
        mStepses.addAll(stepses);
        notifyDataSetChanged();
    }

    public class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.adapter_recipe_step_title)
        TextView recipeStepTitle;

        @BindView(R.id.adapter_recipe_step_cardview)
        CardView recipeStepCardView;

        public RecipeStepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            recipeStepCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickedPosition = getAdapterPosition();
            showRecipeStepDetail(clickedPosition);
            notifyDataSetChanged();
        }
    }

    private void showRecipeStepDetail(int selectedPosition) {
        mCallback.onStepSelected(selectedPosition);
    }
}
