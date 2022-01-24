package com.ender.globalmarket.watcher;

import com.ender.globalmarket.data.MarketEvent;
import com.ender.globalmarket.random.MarketEventData;
import com.ender.globalmarket.random.RandomEventHandler;
import com.ender.globalmarket.random.subhandler.MarketXHandler;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;
//随机事件
public class RandomEventWatcher extends TimerTask {

    @Override
    public void run() {
        List<MarketEvent> marketEvents = MarketEventData.getALLEvents();
        int[] mark = new int[100000];
        int counter = 0;
        for (int i = 0; i <= marketEvents.size(); i++) {
            for (int j = 0; j < marketEvents.get(i).weight; j++) {
                mark[j] = i;
                counter++;
            }
        }
        Random random = new Random();

        RandomEventHandler randomEventHandler = null;
        MarketEvent event = marketEvents.get(mark[random.nextInt(counter)]);
        switch (event.type) {
            case MARKET_X: {
                 randomEventHandler = new MarketXHandler();
                 break;
            }
            case TAX_RATE:{

                break;
            }
            case BANK_INTEREST_RATE: {
                break;
            }
        }
        assert randomEventHandler != null;
        randomEventHandler.run(event);
    }
}
