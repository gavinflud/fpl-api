# Fantasy Premier League API Wrapper

[![Maven Central](https://img.shields.io/maven-central/v/com.gavinflood.fpl.api/fpl-api?style=flat-square&versionSuffix=2.0.0)](https://repo1.maven.org/maven2/com/gavinflood/fpl/api/fpl-api/2.0.0/)
![GitHub](https://img.shields.io/github/license/gavinflud/fpl-api?style=flat-square)
A Kotlin wrapper around the Fantasy Premier League API.

## Installing

### Maven

```xml

<dependencies>
    <dependency>
        <groupId>com.gavinflood.fpl.api</groupId>
        <artifactId>fpl-api</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
    implementation "com.gavinflood.fpl.api:fpl-api:2.0.0"
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

See the docs folder for a more detailed list of the functionality offered by this API.

## Contributing

Contributions are welcome. Feel free to open a pull request with any features and/or fixes you have developed. 