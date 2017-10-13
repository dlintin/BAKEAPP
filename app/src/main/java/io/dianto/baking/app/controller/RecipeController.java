package io.dianto.baking.app.controller;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.dianto.baking.app.App;
import io.dianto.baking.app.event.RecipeEvent;
import io.dianto.baking.app.model.Recipe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static io.dianto.baking.app.util.Constant.Data.BAKING_APP_URL;


public class RecipeController {
    private EventBus eventBus = App.getInstance().getEventBus();
    private RecipeEvent event = new RecipeEvent();

    public void getRecipes() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(BAKING_APP_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();
                List<Recipe> recipes = Arrays.asList(App.getInstance().getGson().fromJson(jsonResponse, Recipe[].class));
                event.setMessage(response.message());
                event.setRecipes(recipes);
                if (response.code() == 200) {
                    event.setSuccess(true);
                } else {
                    event.setSuccess(false);
                }
                eventBus.post(event);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                event.setMessage(e.getMessage());
                event.setSuccess(false);
                eventBus.post(event);
            }
        });
    }
}