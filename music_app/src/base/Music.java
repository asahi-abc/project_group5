package base;

import java.util.Objects;

public class Music {
	String name;
	String artist;
	String url;
	
	public Music() {
		this.name = null;
		this.artist = null;
		this.url = null;
	}
	
	public Music(String name, String url, String artist) {
		this.name = name;
		this.artist = artist;
		this.url = url;
	}
	
	public String getName() {
		return this.name;
	}

	public String getArtist() {
		return this.artist;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 同値関係を定義するメソッド
	 * すべてのインスタンス変数が同値であればtrue
	 * 
	 * @param o 比較対象のオブジェクト
	 */
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o == null) return false;
		if(!(o instanceof Music)) return false;
		Music r = (Music) o;
		if(this.getName().equals(r.getName()) && 
			this.getArtist().equals(r.getArtist()) &&
			this.getUrl().equals(r.getUrl())) return true;
		
		return false;
	}
	
	public int hashCode() {
		return Objects.hash(this.name, this.url, this.artist);
	}
	
	public String toString() {
		return name + " by " + artist + "\nURL : " + url;
	}
}
