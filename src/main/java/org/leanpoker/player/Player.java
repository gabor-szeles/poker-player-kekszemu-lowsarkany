package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonElement request) {
        JsonObject jobject = request.getAsJsonObject();
        int minRaise = jobject.getAsJsonPrimitive("minimum_raise").getAsInt();
        int pot = jobject.getAsJsonPrimitive("pot").getAsInt();
        int playerStack = 0;
        boolean firstRound = true;
        JsonArray playerCards;
        List<Integer> enemyStacks = new ArrayList<>();
        JsonArray players = jobject.getAsJsonArray("players");
        JsonArray communityCards = jobject.getAsJsonArray("community_cards");

        //start player bet buy in

            int currentBuyIn = jobject.getAsJsonPrimitive("current_buy_in").getAsInt();
            int playerCurrentBet = 0;

        //end player bet buy in
        if (communityCards.size() > 0){
            firstRound = false;
        }
        // get stacks
        for (int i = 0; i < players.size(); i++) {
            JsonObject player = players.get(i).getAsJsonObject();
            int stack = player.getAsJsonPrimitive("stack").getAsInt();
            if(player.get("name").toString().equals("Kekszemu Lowsarkany")){
                playerStack = stack;
                playerCards = player.getAsJsonArray("hole_cards");
                // player current bet
                playerCurrentBet = player.getAsJsonPrimitive("bet").getAsInt();
            } else{
                enemyStacks.add(stack);
            }
        }

        //check cards


        return minRaise;
    }

    public static void showdown(JsonElement game) {
    }

    public static int calculateBet(int odds, boolean firstRound, int currentBuyIn, int playerCurrentBet, int minRaise){
        int bet = 0;
        if(firstRound){
            if(odds == 0){
                if(currentBuyIn == playerCurrentBet){
                    bet = 0;
                } else{
                    bet = currentBuyIn - playerCurrentBet;
                }
            } else{
                bet = currentBuyIn - playerCurrentBet + minRaise;
            }
        }
        return bet;
    }
}
