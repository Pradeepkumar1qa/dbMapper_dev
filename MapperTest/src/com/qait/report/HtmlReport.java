package com.qait.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HtmlReport {

 String FileLocation="";
 File f;
 private int counter;
 PrintWriter pw=null;
 String [] color= {"green","red","blue","yellow"};
 
 public void log(String msg) {System.out.println(msg);}
 
 
 public HtmlReport(String filelocation,String tablename) {
	 FileLocation=filelocation+"\\"+tablename+".html";
	 f=new File(FileLocation);
	
	try {
		log("created file at\t" +FileLocation);
		f.createNewFile();
		log("created file at\t" +FileLocation);
		pw=new PrintWriter(f);
	} catch (IOException e) {
		e.printStackTrace();
	 }
	this.writeHeading(tablename);
    }
 
 public void writeHeading(String tablename) {
	 String Symbol_indicator="<p><span>success Updated</span><span class='box' style='background:"+this.color[0]+"'></span>"
	 		+ "<span>Mismathced</span><span class='box' style='background:"+this.color[1]+"'>"
	 		+ "</span><span>AllreadyMapped</span><span class='box' style='background:"+this.color[2]+"'></span>"
	 		+ "</p>";
	 
	String heading="<html>"
	 		+ "<head>"
	 		+ "<style>h1{color:red;text-align:center;font-size=20px;}\n table{width:80%;}tr:nth-child(even){background:seagreen;}"
	 		+ "tr:nth-child(odd){background:grey}td{text-align:center}span h3{float:left;color:blue}"
	 		+ "th{background:yellow}"
	 		+ ".box{width:16px;height:16px;background:green;display:inline-block;margin-left:6px;margin-right:12px;margin-top:6px;}</style>"
	 		+ "</head>"
	 		+ "<body><h1>Class Name\t:"+tablename+"</h1>"+Symbol_indicator+"<table border='1'><tr><th>testcaseid</th><th>teststep</th><th>issue</th></tr>";
	      pw.write(heading);
	      pw.flush();
 }
 /**
  * 
  * @param testid
  * @param step
  * @param flag
  *       -mapped successfully  0
  *       -mismatched    1
  *       -allreadyMapped 2
  */
 public void writeRow(String testid,String step,int flag) {
	 String color=this.color[flag];
	 String template="<tr style=\"background:"+color+"\"><td>"+testid+"</td><td>"+step+"</td><td></td></tr>";
	 //log(template);
	 pw.write(template);
	 pw.flush();
	 counter++;
 }
public void writeLast(ArrayList <String []> arraylist) {
	pw.write("</table>");
	//pw.write("<span><h3>Total method Mapped \t:"+counter+"</span></h3></body></html>");
	write_mismatch(arraylist);
	pw.write("<span><h3>Total method Mapped \t:"+counter+"</span></h3></body></html>");
	pw.flush();
}
public void write_mismatch(ArrayList <String []> arraylist) {
	pw.write("<br><br><br><br>");
	pw.write("<h2>mistmatched function</h2>");
	String buildContentFormismatchTable="<table><tr><td>old_name</td><td>New Name</td><td>Mapping Id</td></tr>";
	String temp="";
	for(String []s:arraylist) {
		temp="<tr><td>"+s[0]+"</td><td>"+s[1]+"</td><td>"+s[2]+"</td></tr>";
		buildContentFormismatchTable+=temp;
	}
	buildContentFormismatchTable+="</table>";
	pw.write(buildContentFormismatchTable);
	
}
// public static void main(String[] args) {
//	HtmlReport html= new HtmlReport("c:\\users\\"+"pradeep kumar"+"\\desktop", "TestStep");
////	html.writeHeading("TestStep");
////	html.writeRow("1525", "firststepforhtmlreport");
////	html.writeRow("1526", "firststepforhtmlreport");
////	html.writeRow("1527", "firststepforhtmlreport");
////	html.writeLast();
// }
}
