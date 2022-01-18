# Managers

This handler exposes data related to all the managers signed playing Fantasy Premier League.

## Usage

`get(managerId: Long)`

Returns a given manager's details, including history events for each game week so far this season.

```kotlin
FantasyAPI.managers.get(3573238)
```

---

`getPlayersForCurrentGameWeek(managerId: Long)`

Returns a list of players in the specified manager's squad for the current game week.

```kotlin
FantasyAPI.managers.getPlayersForCurrentGameWeek(3573238)
```

---

`getRecommendedTransfersForNextGameWeek(managerId: Long, numFreeTransfers: Int)`

Returns a map containing the recommended players that should be sold (key) and replaced with (value) for the next game
week.

```kotlin
FantasyAPI.managers.getRecommendedTransfersForNextGameWeek(3573238, 2)
```

---

`getRecommendedTransfersForNextGameWeek(currentPlayers: List<Player>, numFreeTransfers: Int)`

Returns a map containing the recommended players from the list specified that should be sold (key) and replaced with
(value) for the next game week.

```kotlin
FantasyAPI.managers.getRecommendedTransfersForNextGameWeek(playerList, 2)
```

---

`getRecommendedStartingTeamForNextGameWeek(managerId: Long)`

Returns the best possible team you could select for the upcoming game week with the specified manager's squad based on
total points so far this season, current form, and upcoming fixture difficulty. This uses linear programming to
determine the players that are selected.

```kotlin
FantasyAPI.managers.getRecommendedStartingTeamForNextGameWeek(3573238)
```