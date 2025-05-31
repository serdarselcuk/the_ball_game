package com.allfreeapps.theballgame

import android.util.Log
import com.allfreeapps.theballgame.ui.BallGameViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ExampleUnitTest {
    private lateinit var ballGameViewModel: BallGameViewModel
    private lateinit var logMock: MockedStatic<Log>

//
//    companion object{
//        val testData = TestData()
//        private lateinit var testDataList: List<List<Int>>
//
//        @JvmStatic
//        @BeforeClass
//        fun dataSetup() {
//            testDataList = listOf(
//                testData.horizontal_Up,
//                testData.vertical_left,
//                testData.down_right_leftUpCorner,
//                testData.down_right_rightUpCorner,
//                testData.down_right_leftDownCorner,
//                testData.down_right_rightDownCorner,
//                testData.down_left_leftUpCorner,
//                testData.down_left_rightDownCorner,
//                testData.down_left_leftDownCorner,
//                testData.down_left_rightUpCorner,
//                testData.midCross
//            )
//        }
//    }
//
//    @Before
//    fun setUp() {
//        ballGameViewModel = BallGameViewModel()
//        logMock = Mockito.mockStatic(Log::class.java)
//    }
//
//    @After
//    fun tearDown() {
//        logMock.close()
//    }
//    @Test
//    fun addition_isCorrect() {
//        ballGameViewModel.add3Ball()
//        assertEquals(3, ballGameViewModel.totalBallCount.value)
//        Truth.assertThat(ballGameViewModel.ballList.value.filter { it > 0 }.size).isEqualTo(3)
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully1() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[0]
//            val testColor = 1
//            // Arrange
//            ballGameViewModel.addBall(testData[0], testColor)
//            ballGameViewModel.addBall(testData[1], testColor)
//            ballGameViewModel.addBall(testData[2], testColor)
//            ballGameViewModel.addBall(testData[3], testColor)
//            ballGameViewModel.addBall(testData[4], testColor)
//
//            ballGameViewModel.findColorSeries()
//
//            Truth
//                .assertThat(
//                    ballGameViewModel.getRemovables()
//                ).containsExactly(
//                    testData[0],
//                    testData[1],
//                    testData[2],
//                    testData[3],
//                    testData[4]
//                )
//        }
//
//    @Test
//    fun findColorSeries_executesSuccessfully2() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[1]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully3() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[2]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully4() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[3]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully5() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[4]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully6() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[5]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully7() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[6]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully8() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[7]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully9() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[8]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully10() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[9]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
//    @Test
//    fun findColorSeries_executesSuccessfully11() = runTest {
////        ballGameViewModel.setEmptyBoard()
//        val testData = testDataList[10]
//        val testColor = 1
//        // Arrange
//        ballGameViewModel.addBall(testData[0], testColor)
//        ballGameViewModel.addBall(testData[1], testColor)
//        ballGameViewModel.addBall(testData[2], testColor)
//        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
//
////    @Test
////    fun findColorSeries_executesSuccessfully12() = runTest {
//////        ballGameViewModel.setEmptyBoard()
////        val testData = testDataList[11]
////        val testColor = 1
////        // Arrange
////        ballGameViewModel.addBall(testData[0], testColor)
////        ballGameViewModel.addBall(testData[1], testColor)
////        ballGameViewModel.addBall(testData[2], testColor)
////        ballGameViewModel.addBall(testData[3], testColor)
//        ballGameViewModel.addBall(testData[4], testColor)
//
//        ballGameViewModel.findColorSeries()
//
//        Truth
//            .assertThat(
//                ballGameViewModel.getRemovables()
//            ).containsExactly(
//                testData[0],
//                testData[1],
//                testData[2],
//                testData[3],
//                testData[4]
//            )
//    }
}


