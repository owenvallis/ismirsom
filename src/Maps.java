/* 
	Kohonen's Self Organizing Map implementation

	Based in spirit on Paras Chopra's python implementation
	http://www.paraschopra.com/sourcecode/SOM/index.php

	In this version, we use a 3-element vector and visualize 
	the weights of each node as a color.  To train, we feed
	a random choice of one of ten colors.

	g to go
	p to pause
	r to reset

	5 Nov 08
	Jeffrey J. Guy
	jjg@case.edu
	http://www.jjguy.com/som/
 */

import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Maps extends PApplet implements TuioObserver{

	/**
	 * This is a test to upload libraries
	 */
	private static final long serialVersionUID = 1L;
	ReadFiles data;
	SOM som;
	UpdateText udt;
	Minim minim;
	AudioPlayer song;
	TuioHandler tuioHandler;
	SOMMusicPlayer player;
	SOMButton stop, go, pause, reset, switchButton, trance, techno, house, idm, dnb, downTempo;



	ArrayList<SOMButton> buttons;

	int iter;
	int maxIters = 2000;
	int screenW = 1280;
	int screenH = 750;

	int numberOfXnodes;
	int numberOfYnodes;
	int pixPerNodeW;
	int pixPerNodeH;
	int xNodeOffset;
	int[] d;


	boolean bDebug = true;
	double learnDecay;
	double radiusDecay;
	PFont font12;
	PFont font24;
	boolean bGo = false;
	boolean bPause = false;
	boolean bSwitch = false;
	boolean flag = true;
	String outString;
	float fade = 0;
	int last = 0;
	boolean db1Flag = true;
	boolean db2Flag = true;
	boolean db3Flag = true;
	boolean db4Flag = true;
	boolean db5Flag = true;
	boolean db6Flag = true;
	boolean db7Flag = true;
	boolean db8Flag = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {"--present",  "Maps" });	
	}




	public void setup() 
	{
		size(screenW, screenH+50, P2D);
		background(0);
		frameRate(32);

		//Setup Data
		data = new ReadFiles();
		//Collections.shuffle(data.songs);
		if(d == null){
			d = new int[8];
			for(int x = 0; x < 8; x++){
				d[x] = 1;
			}
		}

		//Setup SOM
		numberOfXnodes = 30;
		numberOfYnodes = 20;
		pixPerNodeW = screenW/ numberOfXnodes;
		pixPerNodeH = screenH/ numberOfYnodes;
		xNodeOffset = (screenW - (pixPerNodeW * numberOfXnodes))/2;

		som = new SOM(numberOfXnodes, numberOfYnodes, 8, this);
		som.initTraining(maxIters);
		iter = 1;
		learnDecay = som.learnRate;
		radiusDecay = max(som.numberOfXnodes, som.numberOfYnodes) / 2;

		minim = new Minim(this);
		player = new SOMMusicPlayer(numberOfXnodes,numberOfYnodes,this);
		song = minim.loadFile(data.songs.get(0).name + ".mp3");

		colorMode(PConstants.RGB, (float) 1.0);
		//Setup Text
		udt = new UpdateText(this);
		font12 = loadFont("Helvetica-12.vlw");
		font24 = loadFont("Helvetica-24.vlw");
		textMode(SCREEN);
		textAlign(LEFT, TOP);		

		// create main program controllers
		tuioHandler = new TuioHandler(this);
		buttons = new ArrayList<SOMButton>();
		stop = new StopButton(this, (float)1.0, (float)0.0, (float)0.0, 850, "Stop");
		buttons.add(stop);
		go = new GoButton(this, (float)0.0, (float)1.0, (float)0.0, 360, "Go!");
		buttons.add(go);
		pause = new PauseButton(this, (float)1.0, (float)1.0, (float)0.0, 400, "Pause");
		buttons.add(pause);
		reset = new ResetButton(this, (float)0.0, (float)0.1, (float)0.9, 440, "Reset");
		buttons.add(reset);
		switchButton = new SwitchButton(this, (float)0.5, (float)0.5, (float)0.5, 320, "Switch");
		buttons.add(switchButton);
		//trance, techno, house, idm, dnb, downTempo
		trance = new GenreButton(this, (float)1.0, (float)0.07843137, (float)0.57647059, (int)(width/44), "Trance");
		buttons.add(trance);
		techno = new GenreButton(this, (float)0.0, (float)0.0, (float)0.80392157, (int)(width/44) + 45, "Techno");
		buttons.add(techno);
		house = new GenreButton(this, (float)0.0235294, (float)0.98431373, (float)0.14117647, (int)(width/44) + 90, "House");
		buttons.add(house);
		idm = new GenreButton(this, (float)0.454902, (float)0.454902, (float)0.454902, (int)(width/44) + 135, "IDM");
		buttons.add(idm);
		dnb = new GenreButton(this, (float)0.2, (float)0.2, (float)0.0, (int)(width/44) + 180, "DNB");
		buttons.add(dnb);
		downTempo = new GenreButton(this, (float)0.4, (float)0.0, (float)0.8, (int)(width/44) + 225, "DTempo");
		buttons.add(downTempo);
	}

	public void draw()
	{
		//used to run, pause, and restart SOM
		if(keyPressed) {
			if (key == 'g' || key == 'G') {
				bGo = true;
				udt.updateText("Go!");
				tuioHandler.removeObserver(this);
			}
			if (key == 'p' || key == 'P') {
				bGo = false;
				udt.updateText("Paused...");      
			}
			if (key == 'r' || key == 'R' ) {
				reset();
			}
			if (key == '1'){
				if(db1Flag){
					if(d[0] == 0){
						d[0] = 1;
					}
					else{
						d[0] = 0;
					}
					//System.out.println(d[0]);
					reset();
					db1Flag = false;
				}
			}
			if (key == '2'){
				if(db2Flag){
					if(d[1] == 0){
						d[1] = 1;
					}
					else{
						d[1] = 0;
					}
					//System.out.println(d[1]);
					reset();
					db2Flag  = false;
				}
			}
			if (key == '3'){
				if(db3Flag){
					if(d[2] == 0){
						d[2] = 1;
					}
					else{
						d[2] = 0;
					}
					//System.out.println(d[2]);
					reset();
					db3Flag  = false;
				}
			}
			if (key == '4'){
				if(db4Flag){
					if(d[3] == 0){
						d[3] = 1;
					}
					else{
						d[3] = 0;
					}
					//System.out.println(d[3]);
					reset();
					db4Flag  = false;
				}
			}
			if (key == '5'){
				if(db5Flag){
					if(d[4] == 0){
						d[4] = 1;
					}
					else{
						d[4] = 0;
					}
					//System.out.println(d[4]);
					reset();
					db5Flag  = false;
				}
			}
			if (key == '6'){
				if(db6Flag){
					if(d[5] == 0){
						d[5] = 1;
					}
					else{
						d[5] = 0;
					}
					//System.out.println(d[5]);
					reset();
					db6Flag  = false;
				}
			}
			if (key == '7'){
				if(db7Flag){
					if(d[6] == 0){
						d[6] = 1;
					}
					else{
						d[6] = 0;
					}
					//System.out.println(d[6]);
					reset();
					db7Flag  = false;
				}
			}
			if (key == '8'){
				if(db8Flag){
					if(d[7] == 0){
						d[7] = 1;
					}
					else{
						d[7] = 0;
					}
					//System.out.println(d[7]);
					reset();
					db8Flag  = false;
				}
			}
		}


		// train the SOM
		if (iter < maxIters && bGo){
			//som.train(iter, rgb);
			som.train(iter);
			iter++;
		}
		if( iter == maxIters && flag){
			bGo = false;
			bPause = false;
			bSwitch = false;
			flag = false;
			som.transferData();
			tuioHandler.registerObserver(this);
		}

		//Draw SOM
		background(0);
		if(bGo || bPause || bSwitch){
			//de-register the listener
			som.render(); 
		}
		else{

			for(int i = 0; i < 30; i++) {
				for(int j = 0; j < 20; j++) {

					float r = (float)((data.songs.get(som.nodes[i][j].trueVectorLocation).r)/255.0);
					float g = (float)((data.songs.get(som.nodes[i][j].trueVectorLocation).g)/255.0);
					float b = (float)((data.songs.get(som.nodes[i][j].trueVectorLocation).b)/255.0);

					fill(r, g, b);
					stroke(0);
					strokeWeight(0);
					rectMode(PConstants.CORNER);
					rect(xNodeOffset+(i*pixPerNodeW), j*pixPerNodeH, pixPerNodeW, pixPerNodeH); 
					//if (bDebug) println("Writing rect ("+r+", "+g+", "+b+") at ("+i*pixPerNodeW+", "+ j*pixPerNodeH+")");
				} // for j
			} // for i



		}

		// bottom bar to hold color recs and text info
		noStroke();
		fill(0);
		rect(0, screenH, screenW, 35);

		stroke(1);
		strokeWeight(1);
		int xpos = (width/2)-(song.bufferSize()/6);
		for (int i = 0; i < song.bufferSize()/3;  i++)
		{
			line(i+xpos, 770 - song.left.get(i)*50, i+xpos, 770 - song.left.get(i+1)*10);
		}
		// draw the position in the song
		// the position is in milliseconds,
		// to get a meaningful graphic, we need to map the value to the range [0, width]
		float x = map(song.position(), 0, song.length(), 0, song.bufferSize()/3);
		stroke(0, (float).1, (float).9);
		strokeWeight(3);
		line(x+xpos, 770 - 20, x+xpos, 770 + 20);

		for(SOMButton b : buttons){
			b.drawButton();
		}
		rectMode(PConstants.CORNER);

		fill((float)0.5);
		rect(890,screenH, 95, 15);
		fill(d[0]);
		rect(900, screenH+5, 5, 5);
		fill(d[1]);
		rect(910, screenH+5, 5, 5);
		fill(d[2]);
		rect(920, screenH+5, 5, 5);
		fill(d[3]);
		rect(930, screenH+5, 5, 5);
		fill(d[4]);
		rect(940, screenH+5, 5, 5);
		fill(d[5]);
		rect(950, screenH+5, 5, 5);
		fill(d[6]);
		rect(960, screenH+5, 5, 5);
		fill(d[7]);
		rect(970, screenH+5, 5, 5);

		//Text info
		fill(1);
		textAlign(LEFT, TOP);
		textFont(font12);
		text("Radius:   "+(float)radiusDecay, (int)(screenW * .88), (int)screenH);
		text("Learning: " +(int)(1000*learnDecay), (int)(screenW * .88), (int)(screenH * 1.02));
		text("Iteration " + iter + "/" +maxIters, (int)(screenW * .88), (int)(screenH * 1.04));

		if (fade > 0) { 
			fill(1, (float)(fade/255.0));
			textFont(font24);    
			text(outString, (int)(screenW * .695), (int)(screenH * 1.035));
		}

		fade -= (millis() - last) / 7;
		last = millis();  
	}

	public void stop()
	{
		song.close();
		minim.stop();

		super.stop();
	}




	public void tuioCursorAdded(long sessionID, int cursorX, int cursorY) {
		player.playSong(cursorX, cursorY);
	}

	public void tuioCursorRemove(long sessionID) {
		// TODO Auto-generated method stub

	}

	public void tuioCursorUpdate(long sessionID, int cursorX, int cursorY) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(){
		tuioHandler.setCursorX(mouseX);
		tuioHandler.setCursorY(mouseY);
		tuioHandler.mouseEventsClicked();
	}

	public void mouseDragged(){
		tuioHandler.setCursorX(mouseX);
		tuioHandler.setCursorY(mouseY);
		tuioHandler.mouseEventsDragged();
	}

	public void mouseReleased(){
		tuioHandler.mouseEventsReleased();
	}

	public void keyReleased() {
		if (key == '1') {
			db1Flag = true;
		}
		if (key == '2') {
			db2Flag = true;
		}
		if (key == '3') {
			db3Flag = true;
		}
		if (key == '4') {
			db4Flag = true;
		}
		if (key == '5') {
			db5Flag = true;
		}
		if (key == '6') {
			db6Flag = true;
		}
		if (key == '7') {
			db7Flag = true;
		}
		if (key == '8') {
			db8Flag = true;
		}
	}
	
	public void reset(){
		som.numberOfXnodes = numberOfXnodes;
		som.numberOfYnodes = numberOfYnodes;
		som.radius = Math.max(som.numberOfXnodes, som.numberOfYnodes) / 2;
		// create nodes/initilize map
		for(int i = 0; i < som.numberOfXnodes; i++){
			for(int j = 0; j < som.numberOfYnodes; j++) {
				som.nodes[i][j] = new Node(som.inputDimension, som.numberOfXnodes, som.numberOfYnodes, this);
				som.nodes[i][j].x = i;
				som.nodes[i][j].y = j;
				som.nodes[i][j].setVector();
			}//for j
		}//for i
		som.initTraining(maxIters);
		iter = 1;
		learnDecay = som.learnRate;
		radiusDecay = max(som.numberOfXnodes, som.numberOfYnodes) / 2;
		song = minim.loadFile(data.songs.get(0).name + ".mp3");
		bGo = false;
		udt.updateText("Reset");
		flag = true;
		tuioHandler.removeObserver(this);
	}

}
