package app.mockExamples

import org.springframework.stereotype.Repository

@Repository
open class DataService {
    open fun retrieveAllData(): IntArray? {
        // Some dummy data
        // Actually this should talk to some database to get all the data
        return intArrayOf(1, 2, 3, 4, 5)
    }
}