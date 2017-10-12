package io.dianto.baking.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.App;
import io.dianto.baking.app.R;
import io.dianto.baking.app.events.Recipe_Step_Event;
import io.dianto.baking.app.model.Ingredients;
import io.dianto.baking.app.model.Steps;
import io.dianto.baking.app.view.adapter.RecipeStepsAdapter;
import io.dianto.baking.app.view.callback.RecipeStep_OnClickListener;

import static io.dianto.baking.app.util.Constant.Data.EXTRA_INGREDIENTS;
import static io.dianto.baking.app.util.Constant.Data.EXTRA_STEPS;

public class RecipeDetailFragment extends Fragment implements RecipeStep_OnClickListener {
    @BindView(R.id.detail_ingredients)
    TextView mDetailIngredients;

    @BindView(R.id.detail_steps)
    RecyclerView mDetailSteps;

    private List<Ingredients> mRecipeIngredientses;
    private List<Steps> mRecipeStepses;

    public RecipeDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        initView(rootView);

        Bundle bundle = getArguments();
        String strIngredients = bundle.getString(EXTRA_INGREDIENTS);
        mRecipeIngredientses = Arrays.asList(App.getInstance().getGson().fromJson(strIngredients, Ingredients[].class));

        String strSteps = bundle.getString(EXTRA_STEPS);
        mRecipeStepses = Arrays.asList(App.getInstance().getGson().fromJson(strSteps, Steps[].class));

        String strIngredient = "";
        for (Ingredients ingredients : mRecipeIngredientses) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredients.getQuantity())
                    + " " + ingredients.getMeasure() + " of " + ingredients.getIngredient() + ".";
            strIngredient += "\n";
        }

        mDetailIngredients.setText(strIngredient);

        LinearLayoutManager recipeStepLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mDetailSteps.setLayoutManager(recipeStepLayoutManager);

        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(this);
        mDetailSteps.setAdapter(recipeStepsAdapter);
        recipeStepsAdapter.setDataAdapter(mRecipeStepses);

        ViewCompat.setNestedScrollingEnabled(mDetailSteps, false);

        return rootView;
    }

    private void initView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onStepSelected(int selectedPosition) {
        EventBus eventBus = App.getInstance().getEventBus();
        Recipe_Step_Event event = new Recipe_Step_Event();
        event.setSelectedPosition(selectedPosition);
        eventBus.post(event);
    }
}