package com.qait.annotation;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.qait.plugin.Mapper;
import com.qait.report.HtmlReport;


public class DbAnnotationProcessor {
	static Class obj=null;
	static Mapper mapper=null;
	public static void domapping(String classname,String dbpath,String htmlreportfilelocation) {
		int total = 0;
		Object o = null;
		String classnamewithpath=classname;
		
		
		try {
			o = Class.forName(classnamewithpath).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			
			e1.printStackTrace();
		}
		
		
		
		obj = o.getClass();
		String cname = obj.getCanonicalName();
		String [] temparray=cname.split("\\.");
		String tableName=temparray[temparray.length-1];
		//System.out.println(tableName);
		
		
		
		
		
		
		
		
		mapper=new Mapper(dbpath,htmlreportfilelocation);
		boolean istablecreated=mapper.createTable(tableName);
		 if(!istablecreated) {
			 System.out.println("table created successfully with name"+tableName);
		 }else {
			 System.out.println("table allready exist in database ");
		 }
		 
		 
		 
		mapper.hardwait(3);
		if(!checkValidityOfClass()) {return;}
		
		for (Method m : obj.getDeclaredMethods()) {
			if (m.isAnnotationPresent(DbMapper.class)) {
				
				DbMapper custom = m.getAnnotation(DbMapper.class);
				//System.out.println(m.getName() + "has annotatin with following value");
				for (String s : custom.testcaseid()) {
					
						
					mapper.insertRow(s,m.getName());
					
					
					//System.out.println(s+"+_tid"+custom.testcaseid());
				}

			}
		
        
	}
		mapper.writeClosingLineInHtml();
		}
   
	public static ArrayList<String> getAllClassnamePresentInPackage(String packagename) {
		ArrayList<String> allclasspresentInpackage=new ArrayList<>();
		
		String path="./src/"+packagename.replace('.','/');
		System.out.println(path);
		File f=new File(path);
	    File[] file=f.listFiles();
	    
	    for (File t:file) {
	    	
	    	allclasspresentInpackage.add(packagename+"."+t.getName().replace(".java","").trim());
	    	//allclasspresentInpackage.add(packagename+t.getName().replace(".java","").trim());
	     }
	     //System.out.println(allclasspresentInpackage);
	    return allclasspresentInpackage;

	}
	
	public static boolean checkValidityOfClass() {
		HashMap<String,String> tempValue=new HashMap<String,String>();
		for (Method m : obj.getDeclaredMethods()) {
			if (m.isAnnotationPresent(DbMapper.class)) {
				
				DbMapper custom = m.getAnnotation(DbMapper.class);
				//System.out.println(m.getName() + "has annotatin with following value");
				for (String s : custom.testcaseid()) {
					if(tempValue.containsKey(s)) {
						System.out.println("testcaseid \t"+s+"\t is allready mapped to method \t"+tempValue.get(s));
						System.out.println("inconsistent class one test case id can't be mapped to multiple class please make it correnct and try again");
						return false;
						
					}
						
					
					tempValue.put(s,m.getName());
					
					//System.out.println(s+"+_tid"+custom.testcaseid());
				}

			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
	ArrayList<String>temp=getAllClassnamePresentInPackage("com.qait.test");
	
	for(String s:temp) {
		
	     DbAnnotationProcessor.domapping(s,"C://sqlite/onboarding.sq3","c:\\users\\"+"pradeep kumar"+"\\desktop\\testreport");
		   
	}
	
	
		
		
		
	}
}
