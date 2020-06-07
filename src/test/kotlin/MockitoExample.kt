import app.Application
import junit.framework.Assert.assertEquals
import app.mockExamples.BusinessService
import app.mockExamples.DataService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class])
class MockitoExample {
    @MockBean
    lateinit var dataServiceMock: DataService

    @Autowired
    lateinit var businessImpl: BusinessService

    @Test
    fun testFindTheGreatestFromAllData() {

        `when`(dataServiceMock.retrieveAllData()).thenReturn(intArrayOf(24, 15, 3))
        assertEquals(24, businessImpl.findTheGreatestFromAllData())
    }

    @Test
    fun testFindTheGreatestFromAllData_ForOneValue() {
        `when`(dataServiceMock.retrieveAllData()).thenReturn(intArrayOf(15))
        assertEquals(15, businessImpl.findTheGreatestFromAllData())
    }

    @Test
    fun testFindTheGreatestFromAllData_NoValues() {
        `when`(dataServiceMock.retrieveAllData()).thenReturn(intArrayOf())
        assertEquals(Int.MIN_VALUE, businessImpl.findTheGreatestFromAllData())
    }


}