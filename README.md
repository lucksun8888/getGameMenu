# getGameMenu
Get batocera or emuelec's gamelist,and touch a menu boot with pdf.
根据batocera或者ee系统的游戏目录xml文件，解析其中文件名称、游戏名称、游戏缩略图信息，自动生成一个pdf文件，文件以列表形式展示所有目标目录下的已注册游戏。
注意：1 默认使用了windows的微软雅黑字体，所以，如果要在batocera上面直接运行，除了要jdk意外还需要将字体文件一并复制过去。
2 因为解析是的xml文件，如果游戏没有在xml中注册，则无法获取到游戏信息。
