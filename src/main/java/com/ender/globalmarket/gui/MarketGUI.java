package com.ender.globalmarket.gui;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.economy.MarketData;
import com.ender.globalmarket.economy.MarketEconomy;
import com.ender.globalmarket.money.Vault;
import com.ender.globalmarket.player.PlayerRegData;
import com.mysql.cj.BindValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class MarketGUI {

    public static List<String> GUIName = List.of("全球市场", "贸易");


    public static List<String> getGUIName() {
        return GUIName;
    }

//    public static List<Inventory> inventoryList;
    //解析全球市场GUI的物品
    public static Material parseMaterial(ItemStack itemStack) {
        String[] name = itemStack.getItemMeta().getLore().get(2).split("：");
        return Material.matchMaterial(name[1]);
    }
    //解析贸易GUI的物品
    public static Material parseTradeMaterial(String title) {
        String[] name = title.split("：");
        return Material.matchMaterial(name[1]);
    }
    //解析全球市场GUI现在所在的页数
    public static int parsePageNowOn(String title) {
        String[] title1 = title.split("场");
        String[] title2 = title1[1].split("/");
        return Integer.parseInt(title2[0]);
    }
    //解析全球市场GUI的总页数
    public static int parsePageTotal(String title) {
        String[] title1 = title.split("场");
        String[] title2 = title1[1].split("/");
        return Integer.parseInt(title2[1]);
    }
    //解析贸易GUI的上一级页面页数
    public static int parsePageFrom(ItemStack button) {
        String[] name = button.getItemMeta().getDisplayName().split("回");
        return Integer.parseInt(name[1]);
    }
    //创建全球市场GUI
    public static List<Inventory> create(Player player) {
        List<Inventory> inv = new ArrayList<>();
        List<MarketItem> marketItems = MarketData.getAllMarketItems();



        int pages = (int) Math.ceil((double) marketItems.size() / (double) (5 * 9));

        int item_counter = 0;

        for (int i = 0; i < pages; i++) {
            inv.add(Bukkit.createInventory(player, 6 * 9, String.format("全球市场%s/%s", i + 1, pages)));
//            int singlePageLim = (i == pages - 1) ? (marketItems.size() % (5 * 9)) : (5 * 9);

            //上一页按钮
            ItemStack left = new ItemStack(Material.LAPIS_LAZULI);
            ItemMeta leftMeta = left.getItemMeta();
            leftMeta.setDisplayName("上一页");
            left.setItemMeta(leftMeta);

            //中间显示用户信息
            ItemStack playerData = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta playerDataMeta = playerData.getItemMeta();
            playerDataMeta.setLore(List.of(String.format("你的账户余额:$%s", Vault.checkCurrency(player.getUniqueId())), String.format("是否拥有免税特权：%s",  (PlayerRegData.isVIP(player) ? "是" : "否"))));
            playerDataMeta.setDisplayName(String.format("玩家信息:%s", player.getDisplayName()));
            playerData.setItemMeta(playerDataMeta);

            //下一页按钮
            ItemStack right = new ItemStack(Material.REDSTONE);
            ItemMeta rightMeta = right.getItemMeta();
            rightMeta.setDisplayName("下一页");
            right.setItemMeta(rightMeta);

            //空白处要放玻璃板以防玩家物品被吞
            ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta emptyMeta = empty.getItemMeta();
            emptyMeta.setDisplayName("我是空白");
            empty.setItemMeta(emptyMeta);

            inv.get(i).setItem(0, left);
            inv.get(i).setItem(1, empty);
            inv.get(i).setItem(2, empty);
            inv.get(i).setItem(3, empty);
            inv.get(i).setItem(4, playerData);
            inv.get(i).setItem(5, empty);
            inv.get(i).setItem(6, empty);
            inv.get(i).setItem(7, empty);
            inv.get(i).setItem(8, right);

            for (int j = 0; j < 5 * 9 ; j++) {
                if (item_counter >= marketItems.size()) inv.get(i).setItem(j + 9, empty);
                else {
                    ItemStack item = new ItemStack(marketItems.get(item_counter).item, 1);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setLore(Arrays.asList(String.format("库存：%s", marketItems.get(item_counter).x), String.format("单价：$%s", MarketEconomy.formatMoney(MarketEconomy.calculate(marketItems.get(item_counter)))), String.format("名称：%s", marketItems.get(item_counter).item.name())));
                    item.setItemMeta(itemMeta);
                    inv.get(i).setItem(j + 9, item);
                }
                item_counter++;
            }
        }
        return inv;
    }
    //打开全球市场GUI
    public static void open(Player player, int page) {
        player.openInventory(create(player).get(page - 1));
    }
    //关闭GUI
    public static void close(Player player) {
        player.closeInventory();
    }
    //打开贸易GUI
    public static void trade(Player player, MarketItem item, int pageNowOn) {
        Inventory inventory = Bukkit.createInventory(player, 9, String.format("贸易：%s", item.item.name()));

        //物品显示
        ItemStack itemSelf = new ItemStack(item.item);
        ItemMeta selfMeta = itemSelf.getItemMeta();
        selfMeta.setLore(List.of(String.format("库存：%s", item.x), String.format("单价：$%s", MarketEconomy.formatMoney(MarketEconomy.calculate(item))), String.format("名称：%s", item.item.name())));
        itemSelf.setItemMeta(selfMeta);
        //购买按钮
        ItemStack buy = new ItemStack(Material.EMERALD);
        ItemMeta buyMeta = buy.getItemMeta();
        buyMeta.setDisplayName("购买");
        buy.setItemMeta(buyMeta);
        //出售按钮
        ItemStack sell = new ItemStack(Material.REDSTONE);
        ItemMeta sellMeta = sell.getItemMeta();
        sellMeta.setDisplayName("出售");
        sell.setItemMeta(sellMeta);
        //返回按钮
        ItemStack back = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(String.format( "返回%s", pageNowOn));
        back.setItemMeta(backMeta);
        //空白处要放玻璃板以防玩家物品被吞
        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName("我是空白");
        empty.setItemMeta(emptyMeta);

        inventory.setItem(0, itemSelf);
        inventory.setItem(1, empty);
        inventory.setItem(2, empty);
        inventory.setItem(3, buy);
        inventory.setItem(4, empty);
        inventory.setItem(5, sell);
        inventory.setItem(6, empty);
        inventory.setItem(7, empty);
        inventory.setItem(8, back);

        player.openInventory(inventory);
    }
}
