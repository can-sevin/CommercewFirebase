package com.canblack.commercewfirebase.ui

class Products {
    var name:String = ""
    var price:Double = 0.0
    var image:String = ""
    var quantity:Int = 0
    var pid:String = ""
    var desc:String = ""

    constructor():this("","",0.0) {

    }


    constructor(Image: String, Name: String,Price:Double) {
        this.image = Image
        this.name = Name
        this.price = Price
    }
}
