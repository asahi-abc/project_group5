package AccountDB;
import base.*;
import java.sql.*;
import java.util.*;

public class AccountDB {
	static String URL = "jdbc:sqlite:src\\\\accountdb.db";
	static Connection connection = null;
	static PreparedStatement ps = null;
	// ドライバのロード
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.println("ドライバのロードに失敗しました");
		}
	}
	
	public static boolean insertData(String name, Music myMusic) {
		if(existName(name)) {
        	System.out.println(name + " is already exist");
        	return false;
        }
		
		String sql = "INSERT INTO account (name, "
				+ "myMusicName, myMusicUrl, myMusicArtist, "
				+ "favMusicName, favMusicUrl, favMusicArtist) "
				+ "VALUES(?,?,?,?,?,?,?)";
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);

            //データベースにアカウントを作成
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, myMusic.getName());
            ps.setString(3, myMusic.getUrl());
            ps.setString(4, myMusic.getArtist());
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setString(7, null);
            
            // アカウントを作成
            ps.executeUpdate();
            connection.commit();
            ps.close();
            return true;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                connection.close();
            } catch(SQLException e) {
                // クローズが失敗
                System.err.println(e);
            }
        }
        return false;
	}
	
	/**
	 * テーブルを作成するメソッド
	 */
	public static void createTable() {
		try {
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // タイムアウトを30秒にセット

			// accountというテーブルがあれば削除
			// 既に存在するテーブルを作成しようとするとエラーになるため
			statement.executeUpdate("DROP TABLE IF EXISTS account");
			// accountというテーブルを作成
			statement.executeUpdate("CREATE TABLE account(name STRING , myMusicName STRING, myMusicUrl STRING, myMusicArtist STRING, favMusicName STRING, favMusicUrl STRING, favMusicArtist STRING)");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
	
	/**
	 * 名前のアカウントを探すメソッド
	 * 
	 * @param name
	 * @return
	 */
	public static ArrayList<Account> searchAccount(String name) {
		ArrayList<Account> res = new ArrayList<>();
		try {
			String sql = "SELECT * FROM account WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			ps = connection.prepareStatement(sql);
			ps.setString(1,name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				// リザルトセットを読む
				String mmn = rs.getString("myMusicName");
				String mmu = rs.getString("myMusicUrl");
				String mma = rs.getString("myMusicArtist");
				
				String fmn = rs.getString("favMusicName");
				String fmu = rs.getString("favMusicUrl");
				String fma = rs.getString("favMusicArtist");
				
				Account temp = new Account(name, Utils.split(fmn, fmu, fma), new Music(mmn, mmu, mma));
				res.add(temp);
				System.out.println("------------------------------------------------------");
				System.out.println(temp);
				System.out.println("------------------------------------------------------");
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
		return res;
	}
	
	/**
	 * 名前のアカウントが存在するかを確認するメソッド
	 * 
	 * @param name
	 * @return
	 */
	public static boolean existName(String name) {
		try {
			String sql = "SELECT * FROM account WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			ps = connection.prepareStatement(sql);
			ps.setString(1,name);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
		return false;
	}
	
	/**
	 * アカウントを削除するメソッド
	 * @param name
	 */
	public static void deleteAccount(String name) {
		try {
			String sql = "DELETE FROM account WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);

			// データベースから要素を削除
			ps = connection.prepareStatement(sql);
			ps.setString(1, name);
			// アカウントを削除
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
        }
	}
	
	/**
	 * お気に入りの音楽を変更するメソッド
	 * 
	 * @param name
	 * @param favMusic
	 */
	public static void updateFavMusic(String name, HashSet<Music> favMusic) {
		try {
			String sql = "UPDATE account SET favMusicName = ?, favMusicUrl = ?, favMusicArtist = ? WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			ArrayList<String> temp = new ArrayList<>(Utils.join(favMusic));
			
			// 変更後のfavMusicの値をセット
			ps.setString(1, temp.get(0));
			ps.setString(2, temp.get(1));
			ps.setString(3, temp.get(1));
			
			ps.setString(4, name);
			// アップデート
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
	
	/**
	 * 一番の音楽を変更するメソッド
	 * 
	 * @param name
	 * @param myMusic
	 */
	public static void updateMyMusic(String name, Music myMusic) {
		try {
			String sql = "UPDATE account SET myMusicName = ?, myMusicUrl = ?, myMusicArtist = ? WHERE name = ?";
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			
			// 変更後のfavMusicの値をセット
			ps.setString(1, myMusic.getName());
			ps.setString(2, myMusic.getUrl());
			ps.setString(3, myMusic.getArtist());
			
			ps.setString(4, name);
			// アップデート
			ps.executeUpdate();
			connection.commit();
			ps.close();
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
	
	/**
	 * テーブルを削除するメソッド
	 */
	public static void dropTable() {
		try {
			// データベースに接続
			connection = DriverManager.getConnection(URL);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); //タイムアウトを30秒にセット

			// accountというテーブルがあれば削除
			statement.executeUpdate("DROP TABLE IF EXISTS account");
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// クローズが失敗
				System.err.println(e);
			}
		}
	}
}
