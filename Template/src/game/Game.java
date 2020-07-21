package game;
import engine.main.*;
import engine.graphics.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import engine.graphics.mathObjects.Point;
import engine.graphics.mathObjects.Vector;
import engine.graphics.mathObjects.Angle;

public class Game extends Main{
	
	private static final long serialVersionUID = 1524306043384221391L;
	Graphics3D g3d = new Graphics3D();
    Camera camera = new Camera();
    BufferedImage imageForScene;
    
    Angle cameraAngle = new Angle(0,0,0);
    Point cameraPosition = new Point(0,0,-2);
    
    Form basicSquare = new Form();

    public Game(){
    	size(1000,700);
        updateRate(60);
		frameRate(60);
        imageForScene = Graphics3D.createBlankBufferedImage((int)(WIDTH/4),(int)(HEIGHT/4));
		//enableInterpolation();
		init();
		name("Template");
        run();
    }
    
    
    
    public void init() {
    	
    	
    }
	double r = 0;
	int u;
	int rd = 32;
	int bs = 32;
	
	public void update(long currentTime){
		if(u%120==0) {
		basicSquare = new Form();
		basicSquare.setColor(0, 128, 0);
		
		for(int i = (int) (cameraPosition.x-rd);i<(int) (cameraPosition.x+rd);i++) {
			for(int j = (int) (cameraPosition.z-rd);j<(int) (cameraPosition.z+rd);j++) {
				double hei = Math.abs(map1.cloudRand(i, j, bs)-0.5)*2.0;
				double heiz = Math.abs(map1.cloudRand(i, j-1, bs)-0.5)*2.0;
				double heix = Math.abs(map1.cloudRand(i-1, j, bs)-0.5)*2.0;
				
				
				if(hei<0.5) {
					basicSquare.setColor(0, 0, 64);
				}else if(hei<0.52){
					basicSquare.setColor(255, 128, 0);
				}else if(hei<0.8){
					basicSquare.setColor(0, (int)(hei*256.0), 0);
				}else {
					basicSquare.setColor(128,128,128);
				}
				int y = (int)(hei*128.0)-64;
				int yx = (int)(heix*128.0)-64;
				int yz = (int)(heiz*128.0)-64;
				basicSquare.addTriangle(i,y,j,1+i,y,j,i,y,1+j);
				
		    	basicSquare.addTriangle(i,y,1+j,1+i,y,1+j,1+i,y,j);
		    	
		    	basicSquare.setColor(64,64,64);
		    	basicSquare.addTriangle(i,yz,j,i,y,j,i+1,yz,j);
		    	basicSquare.addTriangle(i,y,j,i+1,y,j,i+1,yz,j);
		    	basicSquare.setColor(80,80,80);
		    	basicSquare.addTriangle(i,yx,j+1,i,y,j+1,i,yx,j);
		    	basicSquare.addTriangle(i,y,j+1,i,y,j,i,yx,j);
		    	
			}
		}
		}
		
    	
		r+=0.02;
		u++;
		camera.setAngle(cameraAngle);
		camera.setPosition(cameraPosition);
	}
	
	
	RandomMap map1 = new RandomMap();
	public void draw(Graphics2D g){
		g3d.backgroundColor(0,255,255);
		
		basicSquare.addToScene(new Vector(0,0,0),camera);
        g.drawImage(g3d.render(camera,imageForScene),0,HEIGHT,WIDTH,-HEIGHT,null);

	}
	
	double step = 0.03;
	double limit = 0.4;
	Vector velocity = new Vector(0,0,0);
    
	public void input(KeyBoard keyBoard,Mouse mouse){
		
		
		Vector dir = new Vector(Math.sin(cameraAngle.getXRotation()),0,Math.cos(cameraAngle.getXRotation()) );
        if(keyBoard.W) {
        	dir.scaler(step);
        	velocity.add(dir);
        }
        if(keyBoard.S) {
        	dir.scaler(step);
        	dir.rotate(new Angle(Math.PI, 0, 0));
        	velocity.add(dir);
        }
        if(keyBoard.A) {
        	dir.scaler(step);
        	dir.rotate(new Angle(Math.PI/2, 0, 0));
        	velocity.add(dir);
        }
        if(keyBoard.D) {
        	dir.scaler(step);
        	dir.rotate(new Angle(-Math.PI/2, 0, 0));
        	velocity.add(dir);
        }
        velocity.scaler(0.95);
        
        cameraPosition.translate(velocity);
        
        if(keyBoard.LEFT) cameraAngle.shiftAngle(-0.1, 0, 0);
        if(keyBoard.RIGHT) cameraAngle.shiftAngle(0.1, 0, 0);
        
        if(keyBoard.UP) cameraAngle.shiftAngle(0,0.1, 0);
        if(keyBoard.DOWN) cameraAngle.shiftAngle(0,-0.1, 0);
        
        if(keyBoard.SPACE) cameraPosition.translate(new Vector(0,limit,0));
        if(keyBoard.SHIFT) cameraPosition.translate(new Vector(0,-limit,0));

	}
	
	class RandomMap {
		private int seedx,seedy;
		public RandomMap(){
			setSeed(987319872);
		}
		public void setSeed(int s) {
			seedx = s;
			seedy = s*(s/2)+1;
		}
		public double rand(int x, int y) {
			/*
			* algarthm that takes a x and y and spits out
			 * double between 0 and 1 that is predictable but looks random
			 */
			return (Math.abs(seedx*superProd(x, 5)+seedy*superProd(y, 5))%4095)/4096.0;
		}
		private int superProd(int v, int n) {
			int ans = 1;
			for (int i = 0; i<n; i++) {
				ans*=v;
			}
			return ans;
		}
		public double smoothRand(int x, int y, int size) {
			x+=Integer.MAX_VALUE/2;
			y+=Integer.MAX_VALUE/2;
			//interpolates the x and y linearly
			//get map cords with rand2D
			int xr = x/size;
			int yr = y/size;
			//percentage x and y
			double px = (1.0-(x%size)/((double)size));
			double py = (1.0-(y%size)/((double)size));

			double bl = rand(xr, yr);
			double br = rand(xr+1, yr);
			double valxb = bl*px+br*(1.0-px);
			double tl = rand(xr, yr+1);
			double tr = rand(xr+1, yr+1);
			double valxt = tl*px+tr*(1.0-px);
			return valxb*py+valxt*(1.0-py);
		}
		public double cloudRand(int x, int y, int size) {
			//sum of smoothrand with different amp and freq
			double sum = 0;
			double range = 0;
			int c = 1;
			while (size>0) {
				sum+=smoothRand(x, y, size)/c;
				range+=1.0/c;
				c*=2;
				size/=2;
			}
			return sum/range;
		}
	}

	
	public static void main(String[] args) {
		new Game();
	}
	
}