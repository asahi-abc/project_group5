package base;

public class Main {

	public static void main(String[] args) {
		System.out.println(Utils.split("a,b,c,d,a","1,2,3,4,1","a1,a2,a3,a4,a1"));
		Music a = new Music("1","2","3");
		System.out.println(a.equals(new Music("1","2","3")));
	}

}
