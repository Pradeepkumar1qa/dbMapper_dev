package com.qait.test;

import com.qait.annotation.DbMapper;

public class TestMapper {
     
	
	
@DbMapper(testcaseid= {"2225","9958"})
public void first_function() {
	System.out.println("first");
}
	@DbMapper(testcaseid= {"2225"})
public void second_function() {
		System.out.println("second");
}
	@DbMapper
public void third_function() {
		System.out.println("third");
}
   @DbMapper(testcaseid="9854")
  public void fourth_function() {
		System.out.println("fourth function");
	}
}
