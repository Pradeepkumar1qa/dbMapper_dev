package com.qait.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class HtmlReport {

 String FileLocation="";
 File f;
 private int counter;
 PrintWriter pw=null;
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
	 String heading="<html>"
	 		+ "<head>"
	 		+ "<style>h1{color:red;text-align:center;font-size=20px;}\n table{width:100%;}tr:nth-child(even){background:seagreen;}"
	 		+ "tr:nth-child(odd){background:grey}td{text-align:center}span h3{float:left;color:blue}"
	 		+ "th{background:yellow}</style>"
	 		+ "</head>"
	 		+ "<body><h1>Class Name\t:"+tablename+"</h1><table border='1'><tr><th>testcaseid</th><th>teststep</th><th>issue</th></tr>";
	      pw.write(heading);
	      pw.flush();
 }
 
 public void writeRow(String testid,String step) {
	 String template="<tr><td>"+testid+"</td><td>"+step+"</td><td></td></tr>";
	 //log(template);
	 pw.write(template);
	 pw.flush();
	 counter++;
 }
public void writeLast() {
	pw.write("</table>");
	pw.write("<span><h3>Total method Mapped \t:"+counter+"</span></h3></body></html>");
	pw.flush();
}
 public static void main(String[] args) {
	HtmlReport html= new HtmlReport("c:\\users\\"+"pradeep kumar"+"\\desktop", "TestStep");
	html.writeHeading("TestStep");
	html.writeRow("1525", "firststepforhtmlreport");
	html.writeRow("1526", "firststepforhtmlreport");
	html.writeRow("1527", "firststepforhtmlreport");
	html.writeLast();
 }
}
