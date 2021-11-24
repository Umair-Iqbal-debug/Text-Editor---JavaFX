package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import model.TrailBag;
import model.User;
import model.UserBag;

public class BackUpRestoreTools 
{
	
	
	public static UserBag restoreUserBag()
	{
		UserBag userBag = new UserBag();
		try 
		{
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("data/userBag.dat")));
			userBag = (UserBag) ois.readObject();
			ois.close();
			return userBag;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return userBag;
	}
	
	
	public static void backupUserBag(UserBag userBag)
	{
		try 
		{
			ObjectOutputStream oos= new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("data/userBag.dat",false)));
			oos.writeObject(userBag);
			oos.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			
		}
	}
	
}

