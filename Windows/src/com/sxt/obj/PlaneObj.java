package com.sxt.obj;

import com.sxt.GameWin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlaneObj  extends GameObj {
    @Override
    public Image getImg() {
        return super.getImg();
    }

    public PlaneObj(Image img, int x, int y, int width, int height, double speed, GameWin frame) {
        super(img, x, y, width, height, speed, frame);
        //鼠标移动
        this.frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                PlaneObj.super.x=e.getX()-60;
                PlaneObj.super.y=e.getY()-60;
            }
        });
    }

    public PlaneObj() {
        super();
    }

    @Override
    public void paintSelf(Graphics gImage) {
        super.paintSelf(gImage);
        //我方飞机与敌方BOSS的碰撞检测
        if(this.frame.bossObj!=null &&this.getRec().intersects(this.frame.bossObj.getRec())){
            GameWin.state=3;
        }
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();
    }
}
