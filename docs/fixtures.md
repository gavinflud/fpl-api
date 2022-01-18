# Fixtures

This handler exposes data related to the 380 fixtures that are played throughout the Premier League season.

## Usage

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

`getByGameWeek(gameWeekId: Int)`

Returns all fixtures for a specific game week.

```kotlin
FantasyAPI.fixtures.getByGameWeek(17)
```