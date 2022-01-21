# GlobalMarket
##### A cross-server spigot plugin that simulate market economy.
#### 一个跨服务器的spigot插件，以模拟市场经济的价格浮动的方式，用于解决工业类服务器某些物品产能过高导致服务器经济完全失衡的问题。
#### 本插件已经实现基本GUI和管理员命令
## 使用方法
#### 使用用户命令/globalmarket命令简写为/gm
+ /globalmarket gui 打开图形化市场
+ /globalmarket money 查看账户余额
+ /globalmarket list 查看可交易物品列表
+ /globalmarket sell <物品名称(格式例如DIAMOND)> <数量> 出售一定数量物品，数量输入all可以出售所有此种物品
+ /globalmarket buy <物品名称(格式例如DIAMOND)> <数量> 购买一定数量物品
+ /globalmarket calc [buy/sell] <物品名称(格式minecraft:xxx)> <数量> 计算出售或购买一定数量物品的价格
+ /globalmarket help 查看命令提示
/gm 命令可简写为gm
#### 使用管理员命令/globalmarketadmin 进行管理，命令简写为/gma
+ /globalmarketadmin set <物品库存量> <物品最高价格> <市场稳定指数> [物品名称] 此命令用于向市场中添加新的可交易物品，其中物品名称若不指定则将玩家手中的物品加入市场中
+ /globalmarketadmin remove <物品名称> 从市场中移除某个可交易物品
## 注意事项
+ 以上命令中的物品名称格式统一为大写物品名称，如钻石是DIAMOND，铁锭是IRON_INGOT
+ 插件实现了对mod物品的支持，mod物品的名称格式为MODID_XXX，例如机械动力MOD中的物品安山合金的名称为CREATE_ANDESITE_ALLOY
+ 插件在spigot，paperspigot服务器上运行良好，在杂交端服务器arclight，mohist上通过测试
### 数据库
##### 本插件使用MySQL储存数据，目的是未来增加网页端交易功能，因此您必须为其配置一个MySQL数据库，如果您在这方面是新手不妨使用搜索引擎。
##### 在配置好数据库后找到插件的配置文件config.yml，将您的数据库设置正确写入其中即可正常使用。
## 插件权限列表
+ globalmarket.admin 管理员权限，默认给予OP
+ globalmarket.use 普通用户权限，默认给予所有玩家
