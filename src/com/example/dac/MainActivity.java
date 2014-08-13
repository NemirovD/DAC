package com.example.dac;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	//Calculator does not support BEDMAS
	//will do all calculations from left to right
	private TextView disp;
	private String dispText;
	private boolean doneCalculating;//current not used, was supposed
	//to help make it clear the answer but is no longer needed
	private String dispTextTag = "dispTextTag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		doneCalculating = false;
		
		//Button dot = (Button) findViewById(R.id.budot);
		//dot.setVisibility(View.INVISIBLE);
		disp = (TextView) findViewById(R.id.textView2);
		if(savedInstanceState != null){
			dispText = savedInstanceState.getString(dispTextTag);
			disp.setText(dispText);
		}else{
			dispText = "";
		}
		
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	outState.putString(dispTextTag, dispText);
    	super.onSaveInstanceState(outState);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void numberClick(View v){
		if(doneCalculating){
			clear(v);
			doneCalculating = false;
		}
		
		CharSequence text = ((Button)v).getText();
		dispText += text;
		disp.setText(dispText);
	}
	
	public void operClick(View v){
		if(doneCalculating)
			return;
		CharSequence text = ((Button)v).getText();
		
		
		if(dispText.length()==0){
			return;
		}
		
		
		char lastletter = dispText.charAt(dispText.length()-1);
		if(isOperand(lastletter)){
			dispText = new String(dispText.substring(0, dispText.length()-1));
			dispText += text;
		}else{
			dispText += text;
		}
		disp.setText(dispText);
	}
	
	
	//does most of the possible error checking so that 
	//the function that search the dispText string can
	//be as uncluttered as possible
	public void calculate(View v){
		if(doneCalculating)
			return;
		//if the string is empty do nothing
		if(dispText == ""){return;}
		//checks to see if the last value in the string is an operator
		//returns if it is, otherwise does nothing to the current state
		if(isOperand(dispText.charAt(dispText.length()-1))){
			return;
		}
		
		if(!operatorExists()){
			return;
		}
		calcul();
		//doneCalculating = true;
	}
	
	public void calcul(){
		String opa1 = "";
		String opa2 = "";
		double opa1Double = 0;
		double opa2Double = 0;
		char letter = 0;
		char operator = 0;
		
		boolean opa1set = false;
		
		for(int i = 0; i < dispText.length(); i++){
			letter = dispText.charAt(i);
			if(!opa1set){
				if(isOperand(letter)){
					opa1Double = Double.parseDouble(opa1);
					operator = letter;
					opa1set = true;
				}else{
					opa1 += letter;
				}
			}else{
				if(isOperand(letter)){
					opa2Double = Double.parseDouble(opa2);
					opa1Double = arith(opa1Double,opa2Double,operator);
					operator = letter;
					opa2 = "";
				}else{
					opa2 += letter;
				}
			}
		}
		//after this for loop we will need to perform the operations a final time
		opa2Double = Double.parseDouble(opa2);
		opa1Double = arith(opa1Double,opa2Double,operator);
		dispText = String.valueOf(opa1Double);
		disp.setText(dispText);
	}
	
	public boolean operatorExists(){
		for(int i = 0; i < dispText.length(); i++){
			if(isOperand(dispText.charAt(i))){
				return true;
			}
		}
		return false;
	}
	
	public double arith(double op1, double op2, char operator){
	//simple function to apply an arithmetic operator to two operands
		double out = 0;
		switch(operator){
		case '+':
			out = op1+op2;
			break;
		case '-':
			out = op1-op2;
			break;
		case '×':
			out = op1*op2;
			break;
		case '÷':
			out = op1/op2;
			break;
		}
		return out;
	}
	
	public boolean isOperand(char letter){
		if( letter == '-' ||
				letter == '+' ||
				letter == '×' ||
				letter == '÷'){
			return true;
		}
		return false;
	}
	
	
	public void dotClick(View v){
		//no point in adding a decimal if the
		//string is empty
		if(dispText == ""){return;}
		//splits the dispText string by the list of operators
		//and checks only the last one for decimals
		//this is because we are assuming that all the previous
		//operands are correct because this code will have either
		//run on it or it will have no decimals
		//does nothing if decimals are found
		String ops[] = dispText.split("[+/*-]");
		String toBeChecked = ops[ops.length-1];
		
		for(int i = 0; i < toBeChecked.length(); i++){
			if(toBeChecked.charAt(i)=='.'){
				return;
			}
		}
		dispText += '.';
		disp.setText(dispText);
	}
	
	public void clear(View v){
		dispText = "";
		disp.setText(dispText);
	}
	
	public void deleteLastLetter(View v){
		if(dispText.length()==0){
			return;
		}
		dispText = dispText.substring(0, dispText.length()-1);
		disp.setText(dispText);
	}

}
