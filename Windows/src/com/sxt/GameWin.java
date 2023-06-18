package com.sxt;

import com.sxt.obj.*;
import com.sxt.utils.GameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWin extends JFrame {

    //定义双缓存图片
    Image offScreenImage = null;

    //游戏状态 0未开始 1.游戏中 2.暂停 3.通关失败 4.通关成功
     public static int state = 0;
    //游戏得分
    public  static  int score = 0;
    int width = 600;
    int height = 600;
    //游戏的重绘次数
    int count=1;
    //敌机出现的数量
    int enemyCount=0;



    //创建背景图对象
    BgObj bgObj = new BgObj(GameUtils.bgImg,0,-2000,2);
    //创建我方飞机对象
     public  PlaneObj planeObj = new PlaneObj(GameUtils.planeImg,290,550,20,30,0,this);
     //敌方Boss对象
    public BossObj bossObj=null;

    //窗口的启动方法
    public void launch(){
        //窗口是否可见
        setVisible(true);
        //窗口大小
        setSize(width,height);
        //窗口位置
        setLocationRelativeTo(null);
        //窗口标题
        setTitle("飞机大战");

        //将所有要绘制的游戏物体全部添加到gameObj中
        GameUtils.gameObjList.add(bgObj);
        GameUtils.gameObjList.add(planeObj);


        //游戏的点击启动事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == 1 && state == 0){
                    state = 1;
                    repaint();
                }
            }
        });
        //暂停
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==32){
                    switch (state){
                        case 1:
                            state=2;
                            break;
                        case 2:
                            state=1;
                            break;
                        default:
                    }
                }
            }
        });
        //重复绘制
        while(true){
            if(state==1){
                createObj();
                repaint();
            }

            try {
                Thread.sleep(25);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g){
        //创建和容器一样大小的Image图片
        if(offScreenImage==null){
            offScreenImage=this.createImage(width,height);
        }
        //获得该图片的画布
        Graphics gImage=offScreenImage.getGraphics();
        //填充整个画布
        gImage.fillRect(0,0,width,height);

        if(state == 0) {
            //未开始
            gImage.drawImage(GameUtils.bgImg, 0, 0, null);
            gImage.drawImage(GameUtils.bossImg, 220, 120, null);
            gImage.drawImage(GameUtils.explodeImg, 270, 350, null);
            GameUtils.drawWord(gImage,"点击开始游戏",Color.yellow,40,180,300);
        }
        if(state == 1){
            GameUtils.gameObjList.addAll(GameUtils.explodeObjList);
            //运行中
            for(int i=0;i<GameUtils.gameObjList.size();i++){
                GameUtils.gameObjList.get(i).paintSelf(gImage);
            }
            GameUtils.gameObjList.removeAll(GameUtils.removeList);

        }
        if(state == 3) {
            //游戏失败
            gImage.drawImage(GameUtils.explodeImg, planeObj.getX()+20, planeObj.getY()-10, null, this);
            GameUtils.drawWord(gImage,"GAME OVER",Color.RED,50,180,300);
        }
        if(state == 4) {
            //通关
            gImage.drawImage(GameUtils.explodeImg, bossObj.getX()+30, bossObj.getY(), null, this);
            GameUtils.drawWord(gImage,"游戏通关",Color.green,50,190,300);
        }
        GameUtils.drawWord(gImage,score+"分",Color.GREEN,40,30,100);
        //将缓冲区绘制好的图形整个绘制到容器的画布中
        g.drawImage(offScreenImage,0,0,null);
        count++;
        System.out.println(GameUtils.gameObjList.size());
    }
    //批量创建子弹和敌机
    void createObj() {
        //创建我方子弹对象
        if (count % 10== 0) {
            GameUtils.shellObjList.add(new ShellObj(GameUtils.shellImg, planeObj.getX() +35, planeObj.getY() -16, 14, 29, 10,this));
            GameUtils.gameObjList.add(GameUtils.shellObjList.get(GameUtils.shellObjList.size() -1));
        }
        //创建敌方子弹对象
        if (count % 20== 0 && bossObj!=null) {
            GameUtils.bulletObjList.add(new BulletObj(GameUtils.bulletImg,bossObj.getX()+76,bossObj.getY()+85,15,25,5,this));
           GameUtils.gameObjList.add(GameUtils.bulletObjList.get(GameUtils.bulletObjList.size()-1));
        }
        //创建敌机
        if (count % 13== 0) {
            GameUtils.enemyObjList.add(new EnemyObj(GameUtils.enemyImg,(int)(Math.random()*12)*50,0,49,36,5,this));
            GameUtils.gameObjList.add(GameUtils.enemyObjList.get(GameUtils.enemyObjList.size()-1));
            enemyCount++;
        }
        if(enemyCount>50&& bossObj==null){
            bossObj=new BossObj(GameUtils.bossImg,250,35,155,100,5,this);
            GameUtils.gameObjList.add(bossObj);
        }
    }

    public static void main(String[] args) {
        GameWin gameWin = new GameWin();
        gameWin.launch();
    }
}
