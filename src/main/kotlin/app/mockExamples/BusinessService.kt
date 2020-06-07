package app.mockExamples

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class BusinessService {

    @Autowired
    private var dataService: DataService? = null

    open fun findTheGreatestFromAllData(): Int {
        val data = dataService!!.retrieveAllData()
        var greatest = Int.MIN_VALUE
        for (value in data!!) {
            if (value > greatest) {
                greatest = value
            }
        }
        return greatest
    }
}