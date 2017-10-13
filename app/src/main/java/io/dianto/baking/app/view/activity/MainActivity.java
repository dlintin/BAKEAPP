package io.dianto.baking.app.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dianto.baking.app.App;
import io.dianto.baking.app.R;
import io.dianto.baking.app.controller.RecipeController;
import io.dianto.baking.app.event.RecipeEvent;
import io.dianto.baking.app.model.Recipe;
import io.dianto.baking.app.view.adapter.RecipesAdapter;
import io.dianto.baking.app.view.callback.RecipeOnClickListener;

import static io.dianto.baking.app.util.Constant.Data.EXTRA_RECIPE;
import static io.dianto.baking.app.util.Constant.Data.LIST_DATA;
import static io.dianto.baking.app.util.Constant.Data.LIST_NEED_LOADING;
import static io.dianto.baking.app.util.Constant.Data.LIST_STATE;
import static io.dianto.baking.app.util.Constant.Data.MAIN_COLUMN_WIDTH_DEFAULT;
import static io.dianto.baking.app.util.Constant.Function.nextActivity;

public class MainActivity extends AppCompatActivity implements RecipeOnClickListener {
    @BindView(R.id.main_recipes_refresh)
    SwipeRefreshLayout mMainRecipesRefresh;

    @BindView(R.id.main_recipes)
    RecyclerView mRvRecipes;

    @BindView(R.id.main_progress_layout)
    RelativeLayout mProgressLayout;

    @BindView(R.id.main_error_layout)
    RelativeLayout mErrorLayout;

    private RecipesAdapter mRecipeAdapter;
    private EventBus eventBus;
    private boolean mNeedReload = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventBus = App.getInstance().getEventBus();

        initView();

        if (savedInstanceState != null) {
            mRvRecipes.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE));
            mRecipeAdapter.setDataAdapter(Arrays.asList(App.getInstance().getGson().fromJson(savedInstanceState.getString(LIST_DATA), Recipe[].class)));
            mNeedReload = savedInstanceState.getBoolean(LIST_NEED_LOADING);
        }

        mMainRecipesRefresh.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        mMainRecipesRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMainRecipesRefresh.setRefreshing(false);
                getRecipes();
            }
        });

        if (mNeedReload) getRecipes();
    }

    private void getRecipes() {
        mRvRecipes.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);

        RecipeController controller = new RecipeController();
        controller.getRecipes();
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

    private void initView() {
        ButterKnife.bind(this);

        int columns = getColumnCountByWidth();

        RecyclerView.LayoutManager recipeLayoutManager = new GridLayoutManager(this, columns, LinearLayoutManager.VERTICAL, false);
        mRvRecipes.setLayoutManager(recipeLayoutManager);

        mRecipeAdapter = new RecipesAdapter(this);
        mRvRecipes.setAdapter(mRecipeAdapter);
    }

    private int getColumnCountByWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / MAIN_COLUMN_WIDTH_DEFAULT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE, mRvRecipes.getLayoutManager().onSaveInstanceState());
        outState.putString(LIST_DATA, App.getInstance().getGson().toJson(mRecipeAdapter.getDataAdapter()));
        outState.putBoolean(LIST_NEED_LOADING, mNeedReload);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecipes(RecipeEvent event) {
        mProgressLayout.setVisibility(View.GONE);
        if (event.isSuccess()) {
            mRecipeAdapter.setDataAdapter(event.getRecipes());
            mErrorLayout.setVisibility(View.GONE);
            mRvRecipes.setVisibility(View.VISIBLE);
            mNeedReload = false;
        } else {
            mNeedReload = true;
            mRvRecipes.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RECIPE, App.getInstance().getGson().toJson(recipe));

        nextActivity(this, RecipeActivity.class, bundle, false);
    }
}