package com.example.prototype10;

public class User {

    public String  FirstName,LastName,Gender,Age,EmployeeNumber,ContactNumber,Email,Password,Balance,AUserStatus;

    public User(){
    }

    public User(String firstName, String lastName, String gender, String age, String employeeNumber, String contactNumber, String email, String password, String balance, String AUserStatus) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Gender = gender;
        this.Age = age;
        this.EmployeeNumber = employeeNumber;
        this.ContactNumber = contactNumber;
        this.Email = email;
        this.Password = password;
        this.Balance = balance;
        this.AUserStatus = AUserStatus;
    }
}

