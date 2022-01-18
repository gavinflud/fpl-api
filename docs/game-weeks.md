# Game Weeks

This handler exposes data related to all the game weeks that group fixtures together over the course of the Premier
League season.

## Usage

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

`getLatestFinishedGameWeek()`

Returns the latest game week marked as "finished".

```kotlin
FantasyAPI.gameWeeks.getLatestFinishedGameWeek()
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