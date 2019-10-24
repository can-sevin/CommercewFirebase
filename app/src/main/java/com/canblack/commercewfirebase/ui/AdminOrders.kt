package com.canblack.commercewfirebase.ui

class AdminOrders{

    var Address:String= ""
    var City:String = ""
    var Date:String = ""
    var Name:String = ""
    var Phone:String = ""
    var State:String = ""
    var Time:String = ""
    var TotalPrice:String = ""

    constructor():this("","","","","","","","") {

    }

    constructor(Price:String,Time:String,State:String,Phone:String,Name:String,Date:String,City:String,Adress:String){
        this.Address = Adress
        this.City = City
        this.Date = Date
        this.Name = Name
        this.Phone = Phone
        this.State = State
        this.Time = Time
        this.TotalPrice = Price
    }

}