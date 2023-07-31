# 铁砧面板 - 自定义带传参的指令菜单
![logo](https://raw.githubusercontent.com/Tining123/anvilpanel/master/img/logo.png)
**铁砧面板**是一款创新的Minecraft插件，设计用于让服务器拥有者通过独特的铁砧面板界面创建可定制的、带有参数的命令菜单。该插件的主要特性是利用铁砧界面作为输入窗口，从而提供一种更为沉浸式的游戏内管理体验。

## 特性：
- **铁砧界面**：所有插件操作都通过铁砧界面完成。无需进行任何复杂的配置。一切都可以通过简单的GUI进行管理。
- **可定制的命令菜单**：服务器拥有者可以创建带有参数的自己的命令菜单。
- **基于GUI的操作**：所有操作都可以通过GUI完成。无需进行任何文件处理或控制台命令。
- **兼容性**: 无论是否需要传参的命令，都可以通过这个插件方便配置。 
- **权限管理**: 通过GUI，可以非常方便地开关或配置权限组。 甚至可以自定义权限组。

## 使用方法
#### 使用用户命令/anvilpanel 命令简写为/ap
+ /ap use [指令] - 执行一个配好了的指令
+ 
#### 使用管理员命令/anvilpaneladmin 命令简写为/apadmin
+ /apadmin list - 展示所有的指令
+ /apadmin create - 创建一个指令
+ /apgroup group - 管理权限组

## Example
![craete](https://github.com/Tining123/AnvilPanel/blob/main/img/create.gif?raw=true)
![use](https://github.com/Tining123/AnvilPanel/blob/main/img/use.gif?raw=true)
![use](https://github.com/Tining123/AnvilPanel/blob/main/img/setting.gif?raw=true)

您只需要创建指令后，通过list命令就可以配置所有您所需的内容

## 插件权限列表
+ anvilpanel.use 需要给玩家组这个权限才能使用
+ 例如使用Groupmanager命令 /mangaddp builder anvilpanel.use 或者 /mangaddp default anvilpanel.use
+ luckperm则可以直接通过 /lp editor界面添加

## config.yml
+ lang: 手动设置语言

## Developing
+ 全功能统一入口 [-]
+ 自定义标志 [-]
+ 为每一个命令自定义用户群 [-]
+ 可以整合所有命令的自定义菜单 [-]
## About
+ 采用MIT协议，欢迎提建议，issue，fork或者直接下载使用
+ 如果有问题或者需求可以联系QQ 1340212468
## Contact
- Github: https://github.com/Tining123/DemonMarket
- Email: tingave201@outlook.com
