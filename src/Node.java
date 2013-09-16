public class Node {

	Maps p; // The parent PApplet that we will render ourselves onto

	int x, y; 
	int weightCount;
	// random vector (can change)
	float [] w; 
	int trueVectorLocation = (int)(Math.random()*599);
	boolean taken = false;

	Node(int n, int X, int Y, Maps p)
	{
		this.p = p;
		x = X;
		y = Y;
		weightCount = n;
		w = new float[weightCount];
	}

	void setVector(){
		//w = p.data.songs.get(trueVectorLocation).dimensions;
		for(int i = 0; i < weightCount; i++) 
		{
			w[i] = p.d[i]*(float) Math.random();
		}
	}

	float getR(){

		float r = (sigmoid(w[0], (float)1.0, (float)10.0)+sigmoid(w[1], (float)1.0, (float)10.0))*(float)0.5;

		return r;

	}
	float getG(){

		float r = (sigmoid(w[2], (float)1.0, (float)10.0)+sigmoid(w[3], (float)1.0, (float)10.0)+sigmoid(w[4], (float)1.0, (float)10.0))*(float)0.32;

		return r;

	}
	float getB(){

		float r = (sigmoid(w[5], (float)1.0, (float)10.0)+sigmoid(w[6], (float)1.0, (float)10.0)+sigmoid(w[7], (float)1.0, (float)10.0))*(float)0.32;

		return r;

	}

	float sigmoid(float x, float normV, float sharpness) {
		x=(x/normV*2-1)*5*sharpness;
		return 1.0f / (1.0f + (float)Math.exp(-x));
	}

}
