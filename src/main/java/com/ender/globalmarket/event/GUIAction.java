package com.ender.globalmarket.event;

import com.ender.globalmarket.data.Trade;
import com.ender.globalmarket.economy.MarketData;
import com.ender.globalmarket.economy.MarketGetInput;
import com.ender.globalmarket.economy.MarketTrade;
import com.ender.globalmarket.gui.MarketGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

import java.util.List;
import java.util.Objects;

public class GUIAction implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        InventoryView inv = player.getOpenInventory();
        //获取GUI名列表
        List<String> GUINameList = MarketGUI.getGUIName();
        //通过GUI名字判断玩家打开的储存是不是市场GUI,如果是市场GUI则取消玩家的行为，防止玩家拿走GUI中的物品
        for (String GUIName : GUINameList) {
            if (inv.getTitle().contains(GUIName)) {
                e.setCancelled(true);

                switch (GUIName){
                    case "全球市场": {
                        int pageNowOn = MarketGUI.parsePageNowOn(inv.getTitle());
                        int pageTotal = MarketGUI.parsePageTotal(inv.getTitle());
                        switch (e.getSlot()) {
                            case 0: {
                                if (pageNowOn > 1) {//检测如果不是在第一页就可以向前翻页
                                    MarketGUI.close(player);
                                    MarketGUI.open(player, pageNowOn - 1);
                                }
                                break;
                            }
                            case 1:
                            case 2:
                            case 3:
                            case 5:
                            case 6:
                            case 7:
                            case 4: {
                                break;
                            }
                            case 8: {
                                if (pageNowOn < pageTotal) {//检测如果不是在最后一页就可以向后翻页
                                    MarketGUI.close(player);
                                    MarketGUI.open(player, pageNowOn + 1);
                                }
                                break;
                            }
                            default: {
                                if (!e.getCurrentItem().getItemMeta().getDisplayName().equals("我是空白") && e.getCurrentItem() != null)
                                MarketGUI.trade(player, Objects.requireNonNull(MarketData.getMarketItem(MarketGUI.parseMaterial(e.getCurrentItem()))), pageNowOn);//打开交易界面，选择出售还是购买
                            }
                        }
                        break;
                    }
                    case "贸易": {
                        switch (e.getSlot()) {
                            case 0: {
                                break;
                            }
                            case 3: {
                                MarketGUI.close(player);
                                MarketGetInput.message(MarketTrade.type.BUY, player);
                                MarketGetInput.isOnInput.put(player, new Trade(player, MarketTrade.type.BUY, MarketGUI.parseTradeMaterial(inv.getTitle())));
                                break;
                            }
                            case 5: {
                                MarketGUI.close(player);
                                MarketGetInput.message(MarketTrade.type.SELL, player);
                                MarketGetInput.isOnInput.put(player, new Trade(player, MarketTrade.type.SELL, MarketGUI.parseTradeMaterial(inv.getTitle())));
                                break;
                            }
                            case 8: {
                                MarketGUI.close(player);
                                MarketGUI.open(player, MarketGUI.parsePageFrom(e.getCurrentItem()));
                                break;
                            }
                        }
                        break;
                    }
                }

            }
        }



    }
}
