package AccountDB;

import base.*;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		Music myMsc = new Music("The Thrill is gone", 
				"https://www.youtube.com/watch?v=SgXSomPE_FY",
				"B.B. King");
		
		Account myAcc = new Account("noguchi", new HashSet<Music>(), myMsc);
		AccountDB.createTable();
		AccountDB.insertData(myAcc.getName(), myMsc);
		AccountDB.searchAccount("noguchi");
		System.out.println("exist noguchi ? : " + AccountDB.existName("noguchi"));
		System.out.println("exist no ?      : " + AccountDB.existName("no"));
		System.out.println("try to make same name Account");
		System.out.println(AccountDB.insertData("noguchi", new Music()));
		HashSet<Music> temp = new HashSet<>();
		Music a = new Music("a", "1", "a1");
		Music b = new Music("b", "b", "b2");
		temp.add(a);
		temp.add(b);
		
		AccountDB.updateFavMusic("noguchi", temp);
		AccountDB.searchAccount("noguchi");
	}

}
