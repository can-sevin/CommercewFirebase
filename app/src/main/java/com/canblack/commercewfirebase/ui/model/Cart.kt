package com.canblack.commercewfirebase.ui.model

class Cart {
    var pname:String = ""
    var price:Int = 0
    var pid:String = ""
    var quantity:Int = 0
    var discount:String = ""
    var curDate:String = ""

    constructor():this(0.0,"","",0,"","")


    constructor(Price: Double, Name: String,Discount: String,Quantity:Int,Pid:String,CurDate:String){
        this.price = Price.toInt()
        this.pname = Name
        this.discount = Discount
        this.quantity = Quantity
        this.pid = Pid
        this.curDate = CurDate
    }
}
