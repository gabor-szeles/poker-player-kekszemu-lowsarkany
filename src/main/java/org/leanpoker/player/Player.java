package org.leanpoker.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.*;

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

        //start player bet buy in

            int currentBuyIn = jobject.getAsJsonPrimitive("current_buy_in").getAsInt();
            int playerCurrentBet;

        //end player bet buy in
        if (communityCards.size() > 0){
            firstRound = false;
        }

        // get stacks
        for (int i = 0; i < players.size(); i++) {
            int stack = players.get(i).getAsJsonPrimitive("stack").getAsInt();
            if(players.get(i).getAsString("name").equals("Kekszemu Lowsarkany")){
                playerStack = stack;
                playerCards = players.get(i).getAsJsonArray("hole_cards");
                // player current bet
                playerCurrentBet = players.get(i).getAsJsonPrimitive("bet").getAsInt();
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

        SameCardCases(allcards);





        //check cards


        return minRaise;
    }

    public static void showdown(JsonElement game) {
    }


        public static int SameCardCases(JsonArray cards){
            int pair = 0;
            int drill = 0;
            int poker = 0;
            List<String> ranks = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                ranks.add(cards.get(i).getAsString("rank"));
            }

            Set<String> ranksSet = new HashSet<String>(ranks);


            Map<String, Integer> rankCounter = new HashMap<>();
            for (String rank: ranksSet){
                rankCounter.put(rank, Collections.frequency(ranks, rank));
            }
            Iterator it = rankCounter.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry mapElement = (Map.Entry)it.next();
                if (mapElement.getValue().equals(2)){
                    pair += 1;
                }
                if (mapElement.getValue().equals(3)){
                    drill += 1;
                }
                if (mapElement.getValue().equals(4)){
                    poker += 1;
                }

                System.out.println(mapElement.getKey() + " = " + mapElement.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }

            if (pair == 1){
                return 1;
            } else if (pair >= 2) {
                return 2;
            } else if (drill >= 1) {
                return 3;
            } else if (drill == 1 && pair >= 1) {
                return 4;
            } else if (poker == 1){
                return 5;
            } else {
                return 0;
            }

    }

    public static int calculateBet(int odds, boolean firstRound, int currentBuyIn, int playerCurrentBet, int minRaise){
        int bet;
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
