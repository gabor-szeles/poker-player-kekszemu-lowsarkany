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

        int playerStack = 0;
        boolean firstRound = true;
        JsonArray allcards = new JsonArray();
        JsonArray playerCards = new JsonArray();
        List<Integer> enemyStacks = new ArrayList<>();
        JsonArray players = jobject.getAsJsonArray("players");
        JsonArray communityCards = jobject.getAsJsonArray("community_cards");

        //start player bet buy in

            int currentBuyIn = jobject.getAsJsonPrimitive("current_buy_in").getAsInt();
            int playerCurrentBet = 0;

        //end player bet buy in
        if (communityCards.size() > 0){
            firstRound = false;
        }else {
            firstRound = true;
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

        for (int i = 0; i< communityCards.size(); i++){
            allcards.add(communityCards.get(i));
        }
        for (int i = 0; i< playerCards.size(); i++){
            allcards.add(playerCards.get(i));
        }

        int odds = SameCardCases(allcards);
        minRaise = calculateBet(odds, firstRound, currentBuyIn, playerCurrentBet, minRaise, playerStack);

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
                JsonObject actualCard = cards.get(i).getAsJsonObject();
                ranks.add(actualCard.get("rank").toString());
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

    public static int calculateBet(int odds, boolean firstRound, int currentBuyIn, int playerCurrentBet, int minRaise, int stack){
        int bet = 0;
        if(firstRound){
            if(odds == 0){
                if(currentBuyIn == playerCurrentBet){
                    bet = 0;
                } else{
                    bet = currentBuyIn - playerCurrentBet;
                }
            } else{
                bet = currentBuyIn - playerCurrentBet + (int)Math.ceil(stack*0.2);
            }
        } else{
            switch (odds){
                case 0:
                    bet = currentBuyIn - playerCurrentBet;
                    break;
                case 1:
                    bet = currentBuyIn - playerCurrentBet + minRaise;
                    break;
                case 2:
                    bet = currentBuyIn - playerCurrentBet + (int)Math.ceil(stack*0.3);
                    break;
                case 3:
                    bet = currentBuyIn - playerCurrentBet + (int)Math.ceil(stack*0.5);
                    break;
                case 4:
                    bet = currentBuyIn - playerCurrentBet + stack;
                    break;
                case 5:
                    bet = currentBuyIn - playerCurrentBet + stack;
                    break;
            }
        }
        return bet;
    }

}
