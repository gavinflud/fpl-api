# Fantasy Premier League API Wrapper

A Kotlin wrapper around the Fantasy Premier League API.

## Installing

TODO

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

See the documentation section below for a more detailed list of the functionality exposed by the API.

## Documentation

The `FantasyAPI` object exposes the below four services:

### Teams

`get()` *Returns all teams.*

```kotlin
FantasyAPI.teams.get()
```

`get(id: Int)` *Returns a single team.*

```kotlin
FantasyAPI.teams.get(8)
```

### Game Weeks

`get()` *Returns all game weeks.*

```kotlin
FantasyAPI.gameWeeks.get()
```

`get(id: Int)` *Returns a single game week.*

```kotlin
FantasyAPI.gameWeeks.get(12)
```

`getWeekWithHighestScoreToDate()` *Returns the game week with the highest score so far.*

```kotlin
FantasyAPI.gameWeeks.getWeekWithHighestScoreToDate()
```

`getAverageScoreToDate()` *Returns the average score per game week so far.*

```kotlin
FantasyAPI.gameWeeks.getAverageScoreToDate()
```

### Players

`get()` *Returns all players.*

```kotlin
FantasyAPI.players.get()
```

`get(id: Int)` *Returns a single player.*

```kotlin
FantasyAPI.players.get(233)
```

`getByPosition(position: Position)` *Returns all players in the specified position.*

```kotlin
FantasyAPI.players.getByPosition(Position.FORWARD)
```

`getByTeam(id: Int)` *Returns all players in a given team.*

```kotlin
FantasyAPI.players.getByTeam(8)
```

`getTopScoringPlayersByStatType(statType: StatType)` *Returns the top scoring players for a given stat.*

```kotlin
FantasyAPI.players.getTopScoringPlayersByStatType(StatType.GOAL)
```

`getTopScoringPlayersByStatTypeAndPosition(statType: StatType, position: Position)` *Returns the top scoring players in
the specified position for a given stat.*

```kotlin
FantasyAPI.players.getTopScoringPlayersByStatTypeAndPosition(StatType.ASSIST, Position.MIDFIELDER)
```

`getLowestScoringPlayersByStatType(statType: StatType)` *Returns the lowest scoring players for a given stat.*

```kotlin
FantasyAPI.players.getLowestScoringPlayersByStatType(StatType.GOAL)
```

`getLowestScoringPlayersByStatTypeAndPosition(statType: StatType, position: Position)` *Returns the lowest scoring
players in the specified position for a given stat.*

```kotlin
FantasyAPI.players.getLowestScoringPlayersByStatTypeAndPosition(StatType.ASSIST, Position.MIDFIELDER)
```

`getBestValuePlayers(numberToGet: Int)` *Returns the best value players in the league (points / cost).*

```kotlin
FantasyAPI.players.getBestValuePlayers(5)
```

`getBestValuePlayersByPosition(numberToGet: Int, position: Position)` *Returns the best value players in the league in a
specific position (points / cost).*

```kotlin
FantasyAPI.players.getBestValuePlayersByPosition(10, Position.DEFENDER)
```

`getBestPossibleTeamBasedOnCurrentSeason()` *Returns the best possible team you could feasibly build based on total
points so far this season. This uses linear programming to determine the players that are selected.*

```kotlin
FantasyAPI.players.getBestPossibleTeamBasedOnCurrentSeason()
```

### Fixtures

`get()` *Returns all fixtures.*

```kotlin
FantasyAPI.fixtures.get()
```

`get(id: Int)` *Returns a single fixture.*

```kotlin
FantasyAPI.fixtures.get(190)
```

`getByTeam(teamId: Int)` *Returns all fixtures for a specific team.*

```kotlin
FantasyAPI.fixtures.getByTeam(8)
```

## Contributing

Contributions are welcome. Feel free to open a pull request with any features and/or fixes you have developed. 