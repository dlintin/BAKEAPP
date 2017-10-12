package io.dianto.baking.app.controller;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.dianto.baking.app.App;
import io.dianto.baking.app.events.Recipe_Event;
import io.dianto.baking.app.model.Recipes;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static io.dianto.baking.app.util.Constant.Data.BAKING_APP_URL;


public class Recipe_Controller {
    private EventBus eventBus = App.getInstance().getEventBus();
    private Recipe_Event event = new Recipe_Event();

    public void getRecipes() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(BAKING_APP_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();
                List<Recipes> recipes = Arrays.asList(App.getInstance().getGson().fromJson(jsonResponse, Recipes[].class));
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