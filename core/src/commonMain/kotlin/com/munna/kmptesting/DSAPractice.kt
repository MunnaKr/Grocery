package com.munna.kmptesting

fun main() {
    reverseArrayValue()
}

fun reverseArrayValue(){
    val array = intArrayOf(2,3,4,5,6,7)
    var left= 0
    var right = array.size-1

    while (left< right){
        val temp = array[left]
        array[left] = array[right]
        array[right] = temp

        left++
        right--
    }
    print(array.joinToString())
}

fun reverseNumber(){
    val intNumber = 123456
    var num = intNumber
    var reverse = 0
    while (num > 0)
    {
        val mode = num%10
        reverse = reverse * 10 + mode
        num /= 10
    }
    print(reverse.toString())
}
