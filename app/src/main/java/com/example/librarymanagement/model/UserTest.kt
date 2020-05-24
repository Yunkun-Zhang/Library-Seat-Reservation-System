package com.example.librarymanagement.model

//不建议使用Serializable
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

//可以通过alt+Enter自动实现Parcelable的接口
class UserTest() : Parcelable {

    var bar: String? = "test_bar"
    var info: Int = 101


    constructor(parcel: Parcel) : this() {
        bar = parcel.readString()
        info = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bar)
        parcel.writeInt(info)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserTest> {
        override fun createFromParcel(parcel: Parcel): UserTest {
            return UserTest(parcel)
        }

        override fun newArray(size: Int): Array<UserTest?> {
            return arrayOfNulls(size)
        }
    }

}