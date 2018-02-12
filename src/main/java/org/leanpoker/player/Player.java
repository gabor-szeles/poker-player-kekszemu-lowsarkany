package org.leanpoker.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonElement request) {
        JsonObject jobject = request.getAsJsonObject();
        int minRaise = jobject.getAsJsonPrimitive("minimum_raise").getAsInt();

        return minRaise;
    }

    public static void showdown(JsonElement game) {
    }
}
