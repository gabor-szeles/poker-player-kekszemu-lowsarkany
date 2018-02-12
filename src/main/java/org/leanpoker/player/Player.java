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
        int playerStack;

        boolean firstRound = true;
        JsonArray allcards = new JsonArray();
        JsonArray playerCards = new JsonArray();
        List<Integer> enemyStacks = new ArrayList<>();
        JsonArray players = jobject.getAsJsonArray("players");
        JsonArray communityCards = jobject.getAsJsonArray("community_cards");
        if (communityCards.size() > 0){
            firstRound = false;
        }

        // get stacks
        for (int i = 0; i < players.size(); i++) {
            int stack = players.get(i).getAsJsonPrimitive("stack").getAsInt();
            if(players.get(i).getAsString("name").equals("Kekszemu Lowsarkany")){
                playerStack = stack;
                playerCards = players.get(i).getAsJsonArray("hole_cards");
            } else{
                enemyStacks.add(stack);
            }
        }

        for (int i = 0; i< communityCards.size(); i++){
            allcards.add(communityCards.get(i));
        }
        for (int i = 0; i< playerCards.size(); i++){
            allcards.add(playerCards.get(i));
        }

        Pair(allcards);





        //check cards


        return minRaise;
    }

    public static void showdown(JsonElement game) {
    }

        public static int Pair(JsonArray cards){
            List<String> ranks = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                ranks.add(cards.get(i).getAsString("rank"));
            }

            for (int i =0; i
                for (String rank: ranks) {

                }
            }
        return 0;
    }
    //public static int calculateBet(int odds){
//
//    }

}
