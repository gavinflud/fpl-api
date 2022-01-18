package com.gavinflood.fpl.api.domain

import com.gavinflood.fpl.api.serialize.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDateTime

/**
 * Represents a classic league in FPL.
 */
@Serializable
class ClassicLeague private constructor(
    val id: Long,
    val name: String,

    @Serializable(with = LocalDateTimeSerializer::class)
    val dateCreated: LocalDateTime
) {

    @Transient
    private lateinit var initialStandings: ClassicLeagueStandingsWrapper

    @Transient
    private lateinit var retrieveStandings: (pageNumber: Int) -> ClassicLeagueStandingsWrapper

    constructor(
        id: Long,
        name: String,
        dateCreated: LocalDateTime,
        initialStandings: ClassicLeagueStandingsWrapper,
        retrieveStandings: (pageNumber: Int) -> ClassicLeagueStandingsWrapper
    ) : this(id, name, dateCreated) {
        this.initialStandings = initialStandings
        this.retrieveStandings = retrieveStandings
    }

    fun standingsIterator() = ClassicLeagueStandingsCollection(initialStandings, retrieveStandings).iterator()

}

/**
 * Wraps a page of league standings along with a flag indicating if there is a next page.
 */
@Serializable
data class ClassicLeagueStandingsWrapper(
    val standingsList: List<ClassicLeagueStanding>,
    val hasNext: Boolean
)

/**
 * This is a custom collection used to wrap the league standings results.
 *
 * The FPL API only returns 50 results at a time and uses pagination (with separate requests) when more than 50 teams
 * are in a league. As some leagues can have millions of teams in them, this iterator only allows sequential access to
 * the entries and only requests the next 50 results when no all results in memory have been iterated over.
 */
internal class ClassicLeagueStandingsCollection(
    private val initialStandings: ClassicLeagueStandingsWrapper,
    private val retrieveStandings: (pageNumber: Int) -> ClassicLeagueStandingsWrapper
) : Iterable<ClassicLeagueStanding> {


    override fun iterator(): Iterator<ClassicLeagueStanding> {
        return object : Iterator<ClassicLeagueStanding> {

            private val standings = initialStandings.standingsList.toMutableList()
            private var currentIndex = 0
            private var currentPage = 1
            private var hasNextPage = initialStandings.hasNext

            /**
             * Returns true if there are more entries in memory or more pages that the FPL API can return.
             */
            override fun hasNext(): Boolean {
                return currentIndex < standings.size || hasNextPage
            }

            /**
             * Either return the next entry in memory or load the next page and return the first entry from that.
             */
            override fun next(): ClassicLeagueStanding {
                if (currentIndex < standings.size) {
                    currentIndex++
                    return standings[currentIndex - 1]
                } else if (hasNextPage) {
                    currentPage++
                    currentIndex = 0
                    standings.clear()
                    retrieveStandings(currentPage).apply {
                        standingsList.forEach { standings.add(it) }
                        hasNextPage = hasNext
                    }
                    return next()
                }

                throw NoSuchElementException()
            }

        }
    }

}