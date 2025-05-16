package com.allfreeapps.theballgame

data class TestData(
    val horizontal_Up: List<Int> = listOf( 0,1,2,3,4,5,6,7,8),
    val vertical_left: List<Int> = listOf( 0, 9, 18, 27, 36, 45, 54, 63, 72),
    val down_right_leftUpCorner: List<Int> = listOf(0, 10, 20, 30, 40),
    val down_right_rightUpCorner: List<Int> = listOf(4, 14, 24, 34, 44),
    val down_right_leftDownCorner: List<Int> = listOf (36, 46, 56 ,66 ,76),
    val down_right_rightDownCorner: List<Int> =  listOf(31, 41, 51, 61, 71),
    val down_left_leftUpCorner: List<Int> = listOf(4, 12, 20, 28, 36),
    val down_left_rightDownCorner: List<Int> = listOf(44, 52, 60, 68, 76),
    val down_left_leftDownCorner: List<Int> = listOf(31, 39, 47, 55, 63),
    val down_left_rightUpCorner: List<Int> = listOf(8, 16, 24, 32, 40),
    val midCross: List<Int> = listOf( 22, 31, 40, 49, 58, 38, 39, 40, 41, 42)
){

}
