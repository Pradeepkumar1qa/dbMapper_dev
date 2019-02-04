package com.qait.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Annotation;
import com.qait.plugin.Mapper;
import com.qait.report.HtmlReport;


public class DbAnnotationProcessor {
     
	public static void domapping(String classname,String dbpath,String htmlreportfilelocation) {
		int total = 0;
		Object o = null;
		String classnamewithpath=classname;
		
		     //dynamic class loading
		try {
			o = Class.forName(classnamewithpath).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			
			e1.printStackTrace();
		}
		
		//_______________________________class name______________________________________
		
		Class obj = o.getClass();
		String cname = obj.getCanonicalName();
		String [] temparray=cname.split("\\.");
		String tableName=temparray[temparray.length-1];
		//System.out.println(tableName);
		
		//html report for testing_________________________________________________________________________________
		
		
		
		
		
		//_____________________________________________________________________________
		Mapper mapper=new Mapper(dbpath,htmlreportfilelocation);
		boolean istablecreated=mapper.createTable(tableName);
		 if(!istablecreated) {
			 System.out.println("table created successfully with name"+tableName);
		 }else {
			 System.out.println("table allready exist in database ");
		 }
		mapper.hardwait(3);
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
   
	public static void main(String[] args) {
     DbAnnotationProcessor.domapping("com.qait.test.festSecond","C://sqlite/onboarding.sq3","c:\\users\\"+"pradeep kumar"+"\\desktop\\");
	   
	}
}
