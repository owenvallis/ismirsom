public class UpdateText {
	
	Maps p; // The parent PApplet that we will render ourselves onto

	UpdateText(Maps p){
		this.p = p;
	}
	
	void updateText(String s)
	{
	  p.fade = (float)255.0;
	  p.last = p.millis(); 
	  p.outString = s;

	  return;
	}

}
