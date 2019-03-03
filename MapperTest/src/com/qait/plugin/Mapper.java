package com.qait.plugin;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.ws.LogicalMessage;

import com.qait.report.HtmlReport;

/**
 *
 * @author pradeep kumar
 */
public class Mapper {
	String url = "jdbc:sqlite:";
	Connection conn_for_method=null;
	String tablename = null;   //"Testsecond";
	String  rs=null;
	HtmlReport report=null;
	String  htmlreportlocation;
	ArrayList<String[]> mismatchedFunction=new ArrayList<String[]>();
	
	public Mapper(String path,String htmlrepotlocation){
	 this.url=this.url+path;
	 this.conn_for_method = connect();
	 this.htmlreportlocation=htmlrepotlocation;
	 } 
	

	/**
	 * Connect to the test.db database
	 * 
	 * @return the Connection object
	 */

	public static void main(String[] args) {

	}

	private Connection connect() {
		// SQLite connect ion string
		String url = this.url;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
			log("connected successfully");
			return conn;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}

	/**
	 * select all rows in the warehouses table
	 */
	public void selectAll() {
		String sql = "SELECT tcid, teststep FROM \t"+this.tablename;
		PreparedStatement psmt=null;
		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("tcid") + "\t\t\t" + rs.getString("teststep"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @param testcaseid
	 * @param actualfunctionName
	 */
	public void insertRow(String testcaseid, String actualfunctionName) {
		 PreparedStatement psmt=null;
		 String sql = "INSERT INTO\t"+ this.tablename+"\t (tcid,teststep) VALUES(?,?)";
		 //log(testcaseid+"____________"+actualfunctionName);
		  hardwait(3);
       if(isTestCaseIdExist(testcaseid)==1) {
    	   String method_name_in_db_record=methodnameindbrecord();
    	   //log(method_name_in_db_record);
    	   if(method_name_in_db_record.equals(actualfunctionName))
    	   { log(actualfunctionName+"\t method all ready mapped to:\t"+testcaseid);
    	     report.writeRow(testcaseid, actualfunctionName,2);
    	   } else {
    		   log("system found mismatch in mapping name and actual name do you want to replace method name for existin testcase id");
    		   log("[name in db]:\t"+method_name_in_db_record+"\t"+"[actual_name of function]"+actualfunctionName+"\t for "+testcaseid);
    		   String temp[]={method_name_in_db_record,actualfunctionName,testcaseid};
    		   update_row_with_new_name(testcaseid, actualfunctionName, method_name_in_db_record);
    		   mismatchedFunction.add(temp);
    		   log("work still in progress>>>>>>>>>>>>>");
    		   
    	   }
    	   
       }else {
    	   //log("executed for testcaseid:"+testcaseid+"_@");
    	   boolean res=false;
    	  try {
    		  conn_for_method=connect();
    		  psmt= conn_for_method.prepareStatement(sql);
        	  psmt.setString(1, testcaseid);
			  psmt.setString(2, actualfunctionName);
			  wait(5);
			  psmt.execute();
			  res=true;
		} catch (SQLException e) {
						e.printStackTrace();
		} finally {
			if(res) {
    		  log("inserted one row in db \t"+this.tablename+"with testcase id\t"+testcaseid+"step name \t"+actualfunctionName);
    	          report.writeRow(testcaseid, actualfunctionName,0);
			}else {
    		  log("some thing went wrong");
    	      }
			try {
				psmt.close();
				conn_for_method.close();
				log("connection colsed here");
			} catch (SQLException e) {
				//log("116************");
				e.printStackTrace();
			}
    	  }
    	  
       }  
	
	
	}

	public void update_row_with_new_name(String testcaseid,String actualfunctionName,String function_name_in_db_record) {
		 PreparedStatement psmt=null;
		 boolean res=false;
		 String sql = "update \t"+ this.tablename+"\t set teststep='"+actualfunctionName+"'\t where tcid="+ testcaseid+"\t AND teststep='"+function_name_in_db_record+"'";
		 //log(testcaseid+"____________"+actualfunctionName);
		 log(sql);
		  hardwait(3);
	
		 try {
   		  conn_for_method=connect();
   		  psmt= conn_for_method.prepareStatement(sql);
       	  
			  wait(5);
			  psmt.execute();
			  res=true;
		} catch (SQLException e) {
						e.printStackTrace();
		} finally {
			if(res) {
   		  log("inserted one row in db \t"+this.tablename+"with testcase id\t"+testcaseid+"step name \t"+actualfunctionName);
   	          report.writeRow(testcaseid, actualfunctionName,1);
			}else {
   		  log("some thing went wrong");
   	      }
			try {
				psmt.close();
				conn_for_method.close();
				log("connection colsed here");
			} catch (SQLException e) {
				//log("116************");
				e.printStackTrace();
			}
	}
		 }
	

	
	private String methodnameindbrecord() {
		return this.rs;
		
	}


	public boolean createTable(String tablename) {
		Connection conn=null;
		Statement stmt=null;
		String sql1="select * from \t"+tablename;
		String sql = "create table if not exists \t" + tablename + "(\t tcid TEXT, teststep TEXT,issue TEXT )";
		log("creating table with following specification" + sql);

		try {
			conn = this.connect();
			stmt = conn.createStatement();
			stmt.execute(sql1);
			log("table all ready exist with table name"+tablename);
			
			return true;

		} catch (SQLException e) {
			try {
				conn.createStatement().execute(sql);
				log("created new table with table name \t"+tablename);
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			
			return false;
		} finally {
			report=new HtmlReport(htmlreportlocation, tablename);
			this.tablename = tablename;
			try {
				
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("finally")
	public int isTestCaseIdExist(String testcaseid) {
		PreparedStatement psmt=null;
		ResultSet rs=null;
		int isfound=0;
		if (this.tablename == null)
			{ log("please provide table name");
			   return isfound;
			}

		String sql = "select * from\t" + this.tablename + "\t where tcid= ?";

		try {
			conn_for_method=connect();
		    psmt = conn_for_method.prepareStatement(sql);
			//log("Tablename:\t" + this.tablename);
			psmt.setString(1, testcaseid);
		    rs= psmt.executeQuery();
		  

			if (!rs.isBeforeFirst()) {
				log("Not found any test case id with\t" + testcaseid + "\tin\t" + this.tablename);
			      isfound=2;
			}
			if(isfound==0) {
				isfound=1;
			    rs.next();
			    this.rs=rs.getString("teststep");
		    }
		} catch (SQLException e) {
              e.printStackTrace();
		}
		finally {
			  try {
				psmt.close();
				conn_for_method.close();
				
				rs.close();
			} catch (SQLException e) {
				log("excetption arise from line 186");
				e.printStackTrace();
			}  
			 //log("ending of method istescaseExist");
			 return isfound ;
			
		 }
	}
	
	/**
	 * this displaymethod will retrun the teststep name associated with test case id
	 * returns only if a testcase is uniquely associated with a teststep name
	 * 
	 * 
	 * @param rs
	 * @return teststepname if it found otherwise null
	 */
	public String  display(ResultSet rs) {
		int counter=0;
		String tcid=null;
		String teststep=null;
		
		if(rs.equals(null))
			{System.out.println("nothing in result set");
			return null;}
		else 
		{
			try {
				
				while(rs.next()) {
					tcid=rs.getString("tcid");
					teststep=rs.getString("teststep");
					//log(tcid+"------"+teststep);
					}
				    rs.close();
				    counter++;
			}catch (SQLException e) {
				        e.printStackTrace();
			}finally {
				if(counter==1) {
					//System.out.println("just returning from here****2");
					return teststep;
				}else {
					
					return null;
				}
				
			}
		}
	}

	// *********************************************
	public static void log(String msg) {
		
		System.out.println(msg);
		
	}
	
	public void hardwait(int sec) {
//		try {
//			//Thread.sleep(sec*1000);
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		}
	}
	
	public void writeInHtmlFile(String tcid,String step) {
		//this.report.writeRow(tcid, step);
	}


	public void writeClosingLineInHtml() {
		report.writeLast(mismatchedFunction);
		for(String[]s:mismatchedFunction) {
			System.out.println(s[0]+s[1]+s[2]+"#########################################################################################################");
		}
		
	}
	public void wait(int sec) {
		try {
			Thread.sleep(sec*1000);
			log("waited for 5 second");
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}

}
