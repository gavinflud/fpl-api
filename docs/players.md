# Players

This handler exposes data related to all the players playing for the 20 Premier League clubs.

## Usage

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

`getBestPossibleTeamForFreeHit()`

Returns the best possible team you could feasibly build when using your free hit chip. This is based on total points so
far this season, current form, and the difficulty of the next game week. This uses linear programming to determine the
players that are selected.

```kotlin
FantasyAPI.players.getBestPossibleTeamForFreeHit()
```

---

`getRecommendedPlayersByPosition(position: Position, numberToRecommend: Int)`

Returns the highest recommended players for the specified position. This is based on total points so far this season,
current form, and the difficulty of the next game week. This uses linear programming to determine the players that are
selected. The default number to recommend is 5.

```kotlin
FantasyAPI.players.getRecommendedPlayersByPosition(Position.FORWARD, 3)
```

---

`getRecommendedDifferentialsByPosition(position: Position, numberToRecommend: Int, maxOwnedByPercent: Int)`

Returns the highest recommended players that could be differentials for the specified position. This is based on total
points so far this season, current form, and the difficulty of the next game week. This uses linear programming to
determine the players that are selected. The default number to recommend is 5 and the percentage owned by threshold is
10 by default.

```kotlin
FantasyAPI.players.getRecommendedDifferentialsByPosition(Position.FORWARD, 3, 5)
```