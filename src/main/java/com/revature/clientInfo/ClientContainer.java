package com.revature.clientInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientContainer {
	
	
	//necessary container
	private String firstName;
	private String lastName;
	
	private ArrayList<Integer> accounts;
	private Map<Integer, Double> acctBalance;
	private Map<Integer, String> acctTypes;
	
	private String saveOrCheck;
	private String userName;
	private String passWord;
	
	//constructor
	public ClientContainer () {
		accounts = new ArrayList<Integer>();
		acctBalance = new HashMap<Integer, Double>();
		acctTypes = new HashMap<Integer, String>();
	}
	
	
	
	//methods related to the map for balance
	public Double getAcctBalance(Integer accountNum) {
		return acctBalance.get(accountNum);
	}

	public void insertAcctBalance(Integer accountNum, Double Bal) {
		acctBalance.put(accountNum, Bal);
	}

	public void setAcctBalance(Integer accountNum, Double bal) {
		acctBalance.replace(accountNum, bal);
	}
	
	public void rmAcctBalance(int accountNum) {
		acctBalance.remove((Integer) accountNum);
	}
	
	//methods related to the map for type of account
	public String getAcctTypes(Integer accountNum) {
		return acctTypes.get(accountNum);
	}

	public void insertAcctTypes(Integer accountNum, String type) {
		acctTypes.put(accountNum, type);
	}

	public void setAcctTypes(Integer accountNum, String type) {
		acctTypes.replace(accountNum, type);
	}
	public void rmAcctType(int accountNum) {
		acctTypes.remove((Integer) accountNum);
	}
	//method related to the array accounts
	public ArrayList<Integer> getAccounts() {
		return accounts;
	}
	
	public void remAccounts(int accountNumber) {
		this.accounts.remove((Integer) accountNumber);
	}
	
	public void addAccounts(int accountNumber) {
		this.accounts.add(accountNumber);
	}

	public int getInitAccount() {
		return accounts.get(0);
	}
	
	public boolean intValid(int accountNumber) {
		return this.accounts.contains((Integer) accountNumber);
	}
	
	//method related to username
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//method related to password
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
	//method related to first name
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	//method related last name
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	//verify checking or saving
	public String getSaveOrCheck() {
		return saveOrCheck;
	}
	
	public void setSaveOrCheck(String saveOrCheck) {
		this.saveOrCheck = saveOrCheck;
	}
	
}
