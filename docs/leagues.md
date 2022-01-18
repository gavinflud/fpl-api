# Leagues

This handler exposes data related to the various fantasy football leagues (public and private) that exist.

## Usage

`get(leagueId: Long, standingsPage: Int)`

Returns the league details for the given ID and page (defaults to 1). You can access the league standings through the
`standingsIterator()` function on the returned `ClassicLeague` object. Only 50 league standings are returned from the
server at a time, so you can access a specific page of standings using the `standingsPage` parameter (or the iterator
will automatically request subsequent pages needed).

***Warning**: For larger leagues (1500+ entries), this could be extremely slow due to rate limits per minute.*

```kotlin
FantasyAPI.leagues.get(1234567)
```

---

`getMostImprovedManagersSinceLastGameWeek(leagueId: Long, numManagersToShow: Int)`

Returns the manager(s) with the largest difference in points scored when accounting for the last two finished game
weeks. This returns a map with the `Manager` as the key and the difference in points as the value.

```kotlin
FantasyAPI.fixtures.getMostImprovedManagersSinceLastGameWeek(1234567, 3)
```

---

`getLeastImprovedManagersSinceLastGameWeek(leagueId: Long, numManagersToShow: Int)`

Returns the manager(s) with the lowest difference in points scored when accounting for the last two finished game weeks.
This returns a map with the `Manager` as the key and the difference in points as the value.

```kotlin
FantasyAPI.fixtures.getLeastImprovedManagersSinceLastGameWeek(1234567, 3)
```