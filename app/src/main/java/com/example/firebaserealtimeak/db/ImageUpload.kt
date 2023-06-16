package com.example.firebaserealtimeak.db

class ImageUpload {
    var name: String = ""
    var imageUrl: String = ""

    constructor() {
        // Empty constructor needed for Firebase
    }

    constructor(name: String, imageUrl: String) {
        if (name.trim() == "") {
            this.name = "No Name"
        } else {
            this.name = name
        }
        this.imageUrl = imageUrl
    }
}






