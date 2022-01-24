package com.ender.globalmarket.random;

import com.ender.globalmarket.data.MarketEvent;

public interface RandomEventHandler {
    public void run(MarketEvent event);
}
