# Hello
# 我们的沙鹰有开枪动画的！不是直接用远程物品类应付了事的哦
## 这是我的第一个mod项目，之前只有Java基础，花了一周的时间边学边做做出来的，所以比较粗糙敬请谅解...基本都靠MCreator生成大部分代码然后直接上锁自己改
## 很多地方都是闭门造车，肯定不如别的枪械模组（不知道是不是只有我用geckolib来做枪械动画..
## 还有很多TODO：第三人称下的动作还没加，两手枪相同的时候左手不能开火的bug还没修，在服务器开火会同时触发切物品动画的bug还没修..以后再说吧，一周做成这样已经算满意了
## 感谢Ankonzy提供的贴图
## 非常乐意如果有大佬来带我（异想天开）
## 爱发电：https://afdian.net/a/lzypinero
## 简单介绍一下代码，主要部分就是沙鹰父类（FatherDesertEagle）（好奇怪的命名），里面实现了很多子弹管理，保存到NBT，玩家开火和换弹处理
## 然后就是FireProcedure和ReloadProcedure两个类，调试了很久很久，分别在沙鹰父类和ReloadMessage两个类里面被调用
## 其他大部分就是MCreator自己生成的渲染器、模型之类的基础文件