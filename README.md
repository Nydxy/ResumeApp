# ResumeApp 个人简历开发和使用说明文档
An android app for my resume. Requested by my lesson of Android Development.

# 一、概述
本应用使用Android Studio2.3.1开发，编译和目标平台均为Android 7.1.1（技术还是最新的好），最低平台Android 4.0.3。

本应用的开发，遵循了Material Design理念，界面较为美观，布局和使用方式合理。

在个人联系方式的页面，我增加了跳转到QQ、微博的功能（需要手机上有QQ、微博客户端，未安装的话跳转到网页版）。微信由于API限制，不能跳转到我自己的页面，所以没有添加这个功能。

应用的图标是我自定义的，适配了各种dpi，并且有方形和圆形的版本。
    
背景音乐在raw文件夹下，叫background.mp3，大小为3.23MB

个人相册的照片放在drawable文件夹下，共3.8MB。

APK大小为7.91MB，除去音频和图片资源，可执行代码体积为7.91MB-3.23MB-2.71MB=1.97MB。

# 二、主要功能
>1)	能够自动播放背景音乐，并可以通过悬浮按钮暂停。
>2)	分享功能，能够调用系统分享功能，分享到QQ、微信、短信、邮件等。
>3)	能够播放在线视频，可以暂停、快进等。
>4)	摇晃手机可以切换到下一页。
>5)	联系方式，能够一键跳转到手机QQ，并打开我的个人页面。
>6)	联系方式，能够一键跳转到新浪微博客户端，并打开我的个人首页。
>7)	联系方式，能够点击邮箱链接，给我发邮件。
>8)	联系方式，能够点击手机号码，给我发短信、打电话。
>9)	联系方式，能够点击网址，跳转到我的Github。
>10)	我的相册展示。
>11)	横屏自适应。
>12)	网络连接检测
>13)	适配平台Android 7.1.1（Nougat API25），最低Android 4.0.3（API15）

# 三、应用架构
个人简历应用为一个Activity + 4个Fragment模式，框架为AppBar + ViewPager + BottomNavigationView。
AppBar为顶部标题栏，带有一个分享菜单。

ViewPager用于容纳四个不同的页面，内含4个Fragment，这样可以避免使用多个Activity，保持应用的简洁和美观。

BottomNavigationView实现了底部的导航栏，设置了与ViewPager的联动，可以通过点击图标切换页面，也可以滑屏切换页面，还可以摇晃手机切换。

# 四、已知的不足之处
因为时间仓促，应用有以下几点遗憾：

>1)	从竖屏切换为横屏时，MainActivity会销毁，导致背景音乐播放从头开始重新播放。
>2)	图片资源较多，偶尔会有一点点卡顿。
>3)	网络视频播放之前需要等待加载较长的时间。
>4)	在某些情况下（低版本系统缺少解码器）无法播放视频

# 五、二次开发手册
本应用尽量使UI与内容分离，开发者可以较为方便地替换其中的文本、图片等资源。

## 应用图标
>/app/src/main/res/mipmap-hdpi/ic_launcher.png
>
>/app/src/main/res/mipmap-hdpi/ic_launcher_round.png

请务必替换掉从hdpi到xxxhdpi各种dpi下的图标！

## 文本
文本都存放在

>/app/src/main/res/values/strings.xml

请自行修改其中的内容。

## 图片
图片都存放在drawable文件夹

>/app/src/main/res/drawable

注意！

- 替换图片时请保持文件名不变
- 替换之后布局可能需要修改
- 图片不要太大，否则会OOM(OutOfMemory)

## 背景音乐
存放在

>/app/src/main/res/raw/background.mp3

请保持文件名不变

## 在线视频
请在strings.xml中填写你的视频的地址

## 你的个人联系方式

请在strings.xml中填写你的相应的联系方式

--------

请注意本项目使用GPLv3授权！

如果你不知道什么是开源授权协议，简单地说，你可以免费使用本项目代码，可以自由修改，但你必须声明原作者，并同样保持开源。

对于使用本项目源代码造成的一切后果，本人概不负责。

If you have any problem, contact to me via e-mail.
