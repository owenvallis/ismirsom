import java.util.Scanner;


public class Song {

	String name;
	float[] dimensions = new float[8];
	int r,g,b;
	public static final char CR = '\r';
	public static final char LF = '\n';

	Song(String featureData){
		parseString(featureData);
	}

	void parseString(String s) {
		Scanner scanner = new Scanner(s);
		scanner.useDelimiter(" "); 

		for(int x = 0; x < 8; x++) {
			dimensions[x] = Float.valueOf(scanner.next());
		}

		name = scanner.next();
		name = chop(name);
		scanner.close();

		if(name.startsWith("trance")){
			r = 255;
			g = 20;
			b = 147;
		}
		else if(name.startsWith("techno")){
			r = 0;
			g = 0;
			b = 205;
		}
		else if(name.startsWith("house")){
			r = 6;
			g = 251;
			b = 36;
		}
		else if(name.startsWith("idm")){
			r = 116;
			g = 116;
			b = 116;
		}
		else if(name.startsWith("dnb")){
			r = 51;
			g = 51;
			b = 0;
		}
		else if(name.startsWith("downtempo")){
			r = 102;
			g = 0;
			b = 204;
		}
	}


	public static String chop(String str) {
		if (str == null) {
			return null;
		}
		int strLen = str.length();
		if (strLen < 2) {
			return "";
		}
		int lastIdx = strLen - 1;
		String ret = str.substring(0, lastIdx);
		char last = str.charAt(lastIdx);
		if (last == LF) {
			if (ret.charAt(lastIdx - 1) == CR) {
				return ret.substring(0, lastIdx - 1);
			}
		}
		return ret;
	}


}
