package com.server.aqordify;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.security.*;

/**
 * Class that handles communication with the Database
 * @author Ted Malmgren
 *
 */
public class DatabaseConnector {
	private Connection con;
	private Statement state;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat ft;
	private String f;

	/**
	 * Constructor for the class...
	 * @throws SQLException 
	 */
	public DatabaseConnector() {

		//connect to the database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/test", "AC9519", "aqordify123");
			state = con.createStatement();
			//window.println("Connected to database");	

		} catch (Exception e) {
			System.out.println("nope");
			e.printStackTrace();
		}
		cal.getTime();
		ft =  new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		
		//testgrejer
//		String[] pnr = {"077-1122211","099-3366633"};
//		String[] eml = {"as@2slkjdfshdf.sad","rt@pp.oo"};
		addUserToDatabase("krutov", "krutovic", "sdfsdf", "tenor2", pnr, eml);
//		try {
//			createQuire(1, "Crazy Singers", "Sin city");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		checkLogin("as@2slkjdfshdf.sad", "sdfsdf");
//		try {
//			searchForSong("Man on the Moon");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			searchForQuire("aaa");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			addUserToQuire(2,2);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		//	}
//		try {
//			createConcert("swan lake", 2);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * Method that adds a new user to the database
	 */
	public void addUserToDatabase(String name, String surName, String password, String voice, String[] phoneNumber,
			String[] emailAdress) {
		long key = -1L;
		PreparedStatement ps;
		String query = "insert into user (f_name, e_name, password, voice) values ('"+name+"', '"+surName+"', '"+encryptedPassword+"', '"+voice+"')";
		
		try {
			ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
			    key = rs.getLong(1);
			}
			System.out.println(key);
			if(phoneNumber.length > 0){
				for(int i = 0; i < phoneNumber.length; i++){
					query =  "insert into phone (number, user_id) values ('" + phoneNumber[i] + "', '" + key +"')";
					state.execute(query);
				}
	
			}
			if(emailAdress.length > 0){
				for(int i = 0; i < emailAdress.length; i++){
					query =  "insert into email (email, user_id) values ('" + emailAdress[i] + "', '" + key +"')";
					state.execute(query);
				}
	
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
	}

	//method that checks user/password and returns an object containing essential information about that user.
	//if the login is successful that is.
	public User checkLogin(String identifyingMail, String pass) {
		ResultSet rsUser, rsPhone, rsEmail, rsAdmin, rsMember;
		String queryUser = "select id, f_name, e_name, voice from user where id = (select user_id from email where email = '"+identifyingMail+"') and password = '" + pass + "'";
		
		User tmp = null;
		
		System.out.println(queryUser);

		try {
			rsUser = state.executeQuery(queryUser);
			if(rsUser.next()){
				tmp = new User(rsUser.getLong(1),rsUser.getString(2),rsUser.getString(3),rsUser.getString(4),null,null,null,null);
				long id = rsUser.getLong(1);
				
				//get phone posts
				String queryPhone = "select number from phone where user_id = '" +id+"'";
				rsPhone = state.executeQuery(queryPhone);
				int count = 0;
				while(rsPhone.next()){
					count++;
				}
				rsPhone.beforeFirst();
				if(count > 0){
					String[] phoneArr = new String[count];
					for(int i = 0; i < count; i++){
						rsPhone.next();
						phoneArr[i] = rsPhone.getString(1);
					}
					tmp.setPhone(phoneArr);
				}
				
				//get email posts
				String queryEmail = "select email from email where user_id = '" +id+"'";
				rsEmail = state.executeQuery(queryEmail);
				count = 0;
				while(rsEmail.next()){
					count++;
				}
				rsEmail.beforeFirst();
				if(count > 0){
					String[] emailArr = new String[count];
					for(int i = 0; i < count; i++){
						rsEmail.next();
						emailArr[i] = rsEmail.getString(1);
					}
					tmp.setEmail(emailArr);
					
					System.out.println(emailArr[0]);
				}
				
				//get admin posts
				String queryAdmin = "select quire from admin where user = '" +id+"'";
				rsAdmin = state.executeQuery(queryAdmin);
				count = 0;
				while(rsAdmin.next()){
					count++;
				}
				rsAdmin.beforeFirst();
				if(count > 0){
					int[] adminArr = new int[count];
					for(int i = 0; i < count; i++){
						rsAdmin.next();
						adminArr[i] = rsAdmin.getInt(1);
					}
					tmp.setAdmin(adminArr);
					
					System.out.println(adminArr[0]);
				}
				
				//get member posts
				String queryMember = "select quire from quire_member where user = '" +id+"'";
				rsMember = state.executeQuery(queryMember);
				count = 0;
				while(rsMember.next()){
					count++;
				}
				rsMember.beforeFirst();
				if(count > 0){
					int[] memberArr = new int[count];
					for(int i = 0; i < count; i++){
						rsMember.next();
						memberArr[i] = rsMember.getInt(1);
					}
					tmp.setMember(memberArr);
					
					System.out.println(memberArr[0]);
					}
			}
			//Returns the user object if the login was successfull.
			return tmp;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}
	
	public boolean createQuire(int user, String name, String city) throws SQLException{
		PreparedStatement ps;
		int key = -1;

		String queryCreateQuire = "insert into quire(quire_name, quire_city) values ('"+name+"', '"+city+"')";
				ps = con.prepareStatement(queryCreateQuire, Statement.RETURN_GENERATED_KEYS);
		ps.execute();
		ResultSet rs = ps.getGeneratedKeys();
		if (rs != null && rs.next()) {
		    key = (int)(rs.getLong(1));
		    String addQuireToUser = "insert into admin(quire, user) values ('"+key+"', '"+user+"')";
		    state.execute(addQuireToUser);
		    String addForumToQuire = "insert into forum(date_time, entry, quire) values ('"+ft.format(cal.getTime())+"', '"+"Quire created"+"', '"+key+"')";
		    state.execute(addForumToQuire);
		    return true;
		}
		return false;
	}
	
	public boolean addUserToQuire(int quireToAddTo, int userToAdd) throws SQLException{
		String query =  "insert into quire_member (quire, user) values ('" + quireToAddTo + "', '" + userToAdd +"')";
		state.execute(query);
		return true;
	}
	
	public Song[] searchForSong(String searchString) throws SQLException{
		ResultSet rsSong;
		String searchQuery = "select title, composer, rec_by from song where title like '" +searchString+"'";
		rsSong = state.executeQuery(searchQuery);
		int count = 0;
		while(rsSong.next()){
			count++;
		}
		rsSong.beforeFirst();
		if(count > 0){
			Song[] songArr = new Song[count];
			for(int i = 0; i < count; i++){
				rsSong.next();
				songArr[i] = new Song(rsSong.getString(1), rsSong.getString(2), rsSong.getInt(3));
			}
			return songArr;
		}
	return null;
	}
	
	public Quire[] searchForQuire(String searchString) throws SQLException{
		ResultSet rsQuire;
		String searchQuery = "select quire_name, quire_city, user, f_name, e_name from quire inner join admin on quire.id=admin.quire inner join user on user=user.id where quire_name = '" +searchString+"'";
		rsQuire = state.executeQuery(searchQuery);
		int count = 0;
		while(rsQuire.next()){
			count++;
		}
		rsQuire.beforeFirst();
		if(count > 0){
			Quire[] quireArr = new Quire[count];
			for(int i = 0; i < count; i++){
				rsQuire.next();
				quireArr[i] = new Quire(rsQuire.getString(1), rsQuire.getString(2), rsQuire.getInt(3),
						(rsQuire.getString(4)+" " +rsQuire.getString(5)));
			}
			return quireArr;
		}
	return null;
	}
	
	public boolean createConcert(String nameOfConcert, int quire) throws SQLException{
		String query =  "insert into concert (concert_name, quire) values ('" + nameOfConcert + "', '" + quire +"')";
		state.execute(query);
		return true;
	}
	
	public boolean newForumEntry(String entry, int quire) throws SQLException{
	    String addForumToQuire = "insert into forum(date_time, entry, quire) values ('"+ft.format(cal.getTime())+"', '"+entry+"', '"+quire+"')";
	    state.execute(addForumToQuire);
	    return true;
	}
	
} 
