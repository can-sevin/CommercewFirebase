package com.canblack.commercewfirebase.ui

class Products {
    var name:String = ""
    var price:Double = 0.0
    var image:String = ""
    var quantity:Int = 0
    var pid:String = ""
    var desc:String = ""
    var cat:String = ""

    constructor():this("","",0.0,0,"","") {

    }


    constructor(Image: String, Name: String,Price:Double,Quantity:Int,Pid:String,Cat:String){
        this.image = Image
        this.name = Name
        this.price = Price
        this.quantity = Quantity
        this.pid = Pid
        this.cat = Cat
    }
}
