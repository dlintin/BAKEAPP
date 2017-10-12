package io.dianto.baking.app.view.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.App;
import io.dianto.baking.app.R;
import io.dianto.baking.app.controller.Recipe_Controller;
import io.dianto.baking.app.events.Recipe_Event;
import io.dianto.baking.app.model.Ingredients;
import io.dianto.baking.app.model.Recipes;
import io.dianto.baking.app.view.adapter.SimpleRecipesAdapter;
import io.dianto.baking.app.view.callback.SimpleRecipe_OnClickListener;

import static io.dianto.baking.app.util.Constant.Data.EXTRA_RECIPE;
import static io.dianto.baking.app.util.Constant.Data.LIST_DATA;
import static io.dianto.baking.app.util.Constant.Data.LIST_STATE;
import static io.dianto.baking.app.util.Constant.Data.WIDGET_ID;


public class Baking_Widget_Configuration_Activity extends AppCompatActivity implements SimpleRecipe_OnClickListener {
    @BindView(R.id.baking_recipe_list)
    RecyclerView mBakingRecipeList;
    private SimpleRecipesAdapter mAdapter;
    private EventBus eventBus;
    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_widget_configuration);

        eventBus = App.getInstance().getEventBus();

        initView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBakingRecipeList.setLayoutManager(layoutManager);
        mBakingRecipeList.setHasFixedSize(true);

        mAdapter = new SimpleRecipesAdapter(this);
        mBakingRecipeList.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mBakingRecipeList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE));
            mAdapter.setDataAdapter(Arrays.asList(App.getInstance().getGson().fromJson(savedInstanceState.getString(LIST_DATA), Recipes[].class)));
            mAppWidgetId = savedInstanceState.getInt(WIDGET_ID);
            return;
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Recipe_Controller controller = new Recipe_Controller();
        controller.getRecipes();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onRecipeSelected(Recipes recipes) {
        putRecipeToWidget(recipes);
    }

    private void putRecipeToWidget(Recipes recipes) {
        Intent intent = new Intent(this, Recipe_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RECIPE, App.getInstance().getGson().toJson(recipes));
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget_ingredient_layout);

        views.setTextViewText(R.id.widget_ingredients_title, getString(R.string.widget_ingredients_title, recipes.getName()));

        String strIngredient = "";
        for (Ingredients ingredients : recipes.getIngredientses()) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredients.getQuantity())
                    + " " + ingredients.getMeasure() + " of " + ingredients.getIngredient() + ".";
            strIngredient += "\n";
        }

        views.setTextViewText(R.id.widget_detail_ingredients, strIngredient);

        views.setOnClickPendingIntent(R.id.widget_detail_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipes(Recipe_Event event) {
        if (event.isSuccess()) {
            mAdapter.setDataAdapter(event.getRecipes());
        } else {
            Toast.makeText(this, getString(R.string.error_adding_widget), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE, mBakingRecipeList.getLayoutManager().onSaveInstanceState());
        outState.putString(LIST_DATA, App.getInstance().getGson().toJson(mAdapter.getDataAdapter()));
        outState.putInt(WIDGET_ID, mAppWidgetId);
    }
}
