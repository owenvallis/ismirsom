import processing.core.PConstants;


public class SOMButton implements TuioObserver{
	
	Maps p;
	TuioSubject tuioHandler;
	float r, g, b;
	int xPos;
	String name;
	
	public SOMButton(Maps p, float r, float g, float b, int xPos, String name){
		this.p = p;
		this.r = r;
		this.g = g;
		this.b = b;
		this.xPos = xPos;
		this.name = name;
		
		// create main program controllers
		this.tuioHandler = p.tuioHandler;
		this.tuioHandler.registerObserver(this);
	}
	
	public void drawButton(){
	p.rectMode(PConstants.CENTER);
	p.fill(r, g, b);
	p.strokeWeight(0);
	p.rect(xPos, p.screenH+18, 25, 25);
	
	//Text info
	p.fill(1);
	p.textAlign(PConstants.CENTER);
	p.textFont(p.font12);
	p.text(name, xPos, (int)p.screenH + 44);
	}

	public void tuioCursorAdded(long sessionID, int cursorX, int cursorY) {
		// TODO Auto-generated method stub
		
	}

	public void tuioCursorRemove(long sessionID) {
		// TODO Auto-generated method stub
		
	}

	public void tuioCursorUpdate(long sessionID, int cursorX, int cursorY) {
		// TODO Auto-generated method stub
		
	}

}
