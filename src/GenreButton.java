import processing.core.PConstants;


public class GenreButton extends SOMButton{
	
	public GenreButton(Maps p, float r, float g, float b, int pos, String name) {
		super(p, r, g, b, pos, name);
		// TODO Auto-generated constructor stub
		this.tuioHandler.removeObserver(this);
	}
	
	public void drawButton(){
		p.rectMode(PConstants.CENTER);
		p.fill(r, g, b);
		p.strokeWeight(0);
		p.rect(xPos, p.screenH + 15, 20, 20);
		
		//Text info
		p.textAlign(PConstants.CENTER);
		p.textFont(p.font12);
		p.text(name, xPos, (int)p.screenH + 44);
		}

}
