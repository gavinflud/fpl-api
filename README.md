# Fantasy Premier League API Wrapper

[![Maven Central](https://img.shields.io/maven-central/v/com.gavinflood.fpl.api/fpl-api?style=flat-square&versionPrefix=1.0.0)](https://repo1.maven.org/maven2/com/gavinflood/fpl/api/fpl-api/1.0.0/)

A Kotlin wrapper around the Fantasy Premier League API.

## Installing

### Maven

```xml

<dependencies>
    <dependency>
        <groupId>com.gavinflood.fpl.api</groupId>
        <artifactId>fpl-api</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
    implementation "com.gavinflood.fpl.api:fpl-api:1.0.0"
}
```

## Usage

The core class for the API is `FantasyAPI` which is a singleton. With this, you can do things like:

```kotlin
// Get data for all teams
FantasyAPI.teams.get()
```

or

```kotlin
// Get data for a specific player
FantasyAPI.players.get(233)
```

The second example would return a `Player` object for that specific player, something like the following in JSON format:

```json
{
  "id": 233,
  "firstName": "Mohamed",
  "lastName": "Salah",
  "team": {
    "id": 11,
    "name": "Liverpool",
    "overallHomeStrength": 1340,
    "overallAwayStrength": 1350,
    "attackHomeStrength": 1290,
    "attackAwayStrength": 1330,
    "defenceHomeStrength": 1350,
    "defenceAwayStrength": 1360
  },
  "position": "MIDFIELDER",
  "dreamTeamCount": 5,
  "inDreamTeam": true,
  "currentCost": 13.0,
  "selectedByPercentage": 71.8,
  "totalPoints": 125,
  "totalMinutes": 1080,
  "transfersIn": 2479740,
  "transfersOut": 802404
}
```

If none of the services exposed by the `FantasyAPI` object can accommodate what you are trying to do, it also exposes
a `httpClient` which gives you direct access to the Fantasy Premier League API endpoints.

The following example shows how you can get the bootstrap data:

```kotlin
FantasyAPI.httpClient.getGeneralInfo()
```

See the documentation section below for a more detailed list of the functionality exposed by this API.

## Documentation

The `FantasyAPI` object exposes the below five services:

### Teams

`get()`

Returns all teams.

```kotlin
FantasyAPI.teams.get()
```

---

`get(id: Int)`

Returns a single team.

```kotlin
FantasyAPI.teams.get(8)
```

### Game Weeks

`get()`

Returns all game weeks.

```kotlin
FantasyAPI.gameWeeks.get()
```

---

`get(id: Int)`

Returns a single game week.

```kotlin
FantasyAPI.gameWeeks.get(12)
```

---

`getCurrentGameWeek()`

Returns the current game week.

```kotlin
FantasyAPI.gameWeeks.getCurrentGameWeek()
```

---

`getNextGameWeek()`

Returns the next game week.

```kotlin
FantasyAPI.gameWeeks.getNextGameWeek()
```

---

`getNextGameWeeks(numberOfGameWeeks: Int)`

Returns the specified number of game weeks after the current one.

```kotlin
FantasyAPI.gameWeeks.getNextGameWeeks(3)
```

---

`getWeekWithHighestScoreToDate()`

Returns the game week with the highest score so far.

```kotlin
FantasyAPI.gameWeeks.getWeekWithHighestScoreToDate()
```

---

`getAverageScoreToDate()`

Returns the average score per game week so far.

```kotlin
FantasyAPI.gameWeeks.getAverageScoreToDate()
```

---

### Players

`get()`

Returns all players.

```kotlin
FantasyAPI.players.get()
```

---

`get(id: Int)`

Returns a single player.

```kotlin
FantasyAPI.players.get(233)
```

---

`getByPosition(position: Position)`

Returns all players in the specified position.

```kotlin
FantasyAPI.players.getByPosition(Position.FORWARD)
```

---

`getByTeam(id: Int)`

Returns all players in a given team.

```kotlin
FantasyAPI.players.getByTeam(8)
```

---

`getTopScoringPlayersByStatType(statType: StatType)`

Returns the top scoring players for a given stat.

```kotlin
FantasyAPI.players.getTopScoringPlayersByStatType(StatType.GOAL)
```

---

`getTopScoringPlayersByStatTypeAndPosition(statType: StatType, position: Position)`

Returns the top scoring players in the specified position for a given stat.

```kotlin
FantasyAPI.players.getTopScoringPlayersByStatTypeAndPosition(StatType.ASSIST, Position.MIDFIELDER)
```

---

`getLowestScoringPlayersByStatType(statType: StatType)`

Returns the lowest scoring players for a given stat.

```kotlin
FantasyAPI.players.getLowestScoringPlayersByStatType(StatType.GOAL)
```

---

`getLowestScoringPlayersByStatTypeAndPosition(statType: StatType, position: Position)`

Returns the lowest scoring players in the specified position for a given stat.

```kotlin
FantasyAPI.players.getLowestScoringPlayersByStatTypeAndPosition(StatType.ASSIST, Position.MIDFIELDER)
```

---

`getBestValuePlayers(numberToGet: Int)`

Returns the best value players in the league (points / cost).

```kotlin
FantasyAPI.players.getBestValuePlayers(5)
```

---

`getBestValuePlayersByPosition(numberToGet: Int, position: Position)`

Returns the best value players in the league in a specific position (points / cost).

```kotlin
FantasyAPI.players.getBestValuePlayersByPosition(10, Position.DEFENDER)
```

---

`getBestPossibleTeamBasedOnCurrentSeason()`

Returns the best possible team you could feasibly build based on total points so far this season, current form, and
upcoming fixture difficulty. This uses linear programming to determine the players that are selected.

```kotlin
FantasyAPI.players.getBestPossibleTeamBasedOnCurrentSeason()
```

---

### Fixtures

`get()`

Returns all fixtures.

```kotlin
FantasyAPI.fixtures.get()
```

---

`get(id: Int)`

Returns a single fixture.

```kotlin
FantasyAPI.fixtures.get(190)
```

---

`getByTeam(teamId: Int)`

Returns all fixtures for a specific team.

```kotlin
FantasyAPI.fixtures.getByTeam(8)
```

---

### Managers

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

`getRecommendedStartingTeamForNextGameWeek(managerId: Long)`

Returns the best possible team you could select for the upcoming game week with the specified manager's squad based on
total points so far this season, current form, and upcoming fixture difficulty. This uses linear programming to
determine the players that are selected.

```kotlin
FantasyAPI.managers.getRecommendedStartingTeamForNextGameWeek(3573238)
```

---

## Contributing

Contributions are welcome. Feel free to open a pull request with any features and/or fixes you have developed. 