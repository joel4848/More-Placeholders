# More Placeholders

A mod which brings back the Scoreboard, Tag and Team placeholders which were removed in FancyMenu 3.9.0 - but now with server-side support!

## What does More Placeholders do?

FancyMenu 3.9.0 removed the Scoreboard placeholders because the client only gets data for a scoreboard if it's set in a display slot. It also removed placeholders related to tags and teams.

More Placeholders adds these back, **and also syncs scoreboard and tag data to the client** so that the placeholders work!

It also adds a new placeholder: Players With Score (players_with_score). This returns a list of players whose score for a particular objective matches a specified number or range.

## Placeholders

**Scoreboard:**
- Scoreboard Score (scoreboard_score) - _Returns the score of a player for a given scoreboard objective._
- Scoreboard Objectives List (scoreboard_objectives_list) - _Returns a separated list of all scoreboard objective names._
- Scoreboard Tracked Players (scoreboard_tracked_players) - _Returns a separated list of all score holders tracked by the scoreboard (i.e. anyone with at least one score)._
- Scoreboard Display Slot (scoreboard_display_slot) - _Returns the name of the objective currently displayed in the given display slot._
- Scoreboard Display Slot (scoreboard_display_slot) - _Returns the name of the objective currently displayed in the given display slot._
- Scoreboard Has Score (scoreboard_has_score) - _Returns `true` if the given player has a score for the given objective, `false` otherwise._
- Scoreboard Objective Display Name (scoreboard_objective_display_name) - _Returns the display name of a scoreboard objective._
- Scoreboard Objective Criteria (scoreboard_objective_criteria) - _Returns the criteria of a scoreboard objective (e.g. `dummy`, `playerKillCount`)._
- Scoreboard Objective Render Type (scoreboard_objective_render_type) - _Returns the render type of a scoreboard objective — either `integer` or `hearts`._
- Scoreboard Player Scores List (scoreboard_player_scores_list) - _Returns a formatted, separated list of all scores a player has across every objective._
- Scoreboard Objective Count (scoreboard_objective_count) - _Returns the total number of scoreboard objectives._

**Team:**
- Player Team (player_team) - _Returns the scoreboard team name of the specified player. Returns empty if the player is not on a team._

**Tag:**
- Players With Score (players_with_score) - _Returns a separated list of all score holders whose score for a given objective matches a value or range. Uses the same range syntax as Minecraft commands._
- Player Has Tag (player_has_tag) - _Returns `true` if the named player has the given tag (as applied by `/tag`), `false` otherwise._
- Player Tags List (player_tags_list) - _Returns a sorted, separated list of all tags on the named player._

## Usage Guide

> **Note:** Although this mod can be used only on the **client**, for scoreboard and tag placeholders to work fully the mod must also be installed on the **server**. Without the server-side install, these placeholders fall back to whatever vanilla syncs, which is limited.

### Scoreboard

---

**Scoreboard Score (scoreboard_score)**

Returns the score of a player for a given scoreboard objective.

```json
{"placeholder":"scoreboard_score","values":{"player":"Player1","objective":"my_objective"}}
```

Parameters:
- `player`: The name of the score holder
- `objective`: The name of the scoreboard objective

Example output: `42`

---

**Scoreboard Objectives List (scoreboard_objectives_list)**

Returns a separated list of all scoreboard objective names.

```json
{"placeholder":"scoreboard_objectives_list","values":{"separator":", "}}
```

Parameters:
- `separator`: Text to join objective names with (default: `", "`)

Example output: `my_objective, kills, deaths`

---

**Scoreboard Tracked Players (scoreboard_tracked_players)**

Returns a separated list of all score holders tracked by the scoreboard (i.e. anyone with at least one score).

```json
{"placeholder":"scoreboard_tracked_players","values":{"separator":", "}}
```

Parameters:
- `separator`: Text to join names with (default: `", "`)

Example output: `Player1, Player2, Steve`

---

**Scoreboard Display Slot (scoreboard_display_slot)**

Returns the name of the objective currently displayed in the given display slot.

```json
{"placeholder":"scoreboard_display_slot","values":{"slot":"sidebar"}}
```

Parameters:
- `slot`: The display slot to query. Valid values: `list`, `sidebar`, `below_name`

Example output: `my_objective`

---

**Scoreboard Has Score (scoreboard_has_score)**

Returns `true` if the given player has a score for the given objective, `false` otherwise.

```json
{"placeholder":"scoreboard_has_score","values":{"player":"Player1","objective":"my_objective"}}
```

Parameters:
- `player`: The name of the score holder
- `objective`: The name of the scoreboard objective

Example output: `true`

---

**Scoreboard Objective Display Name (scoreboard_objective_display_name)**

Returns the display name of a scoreboard objective.

```json
{"placeholder":"scoreboard_objective_display_name","values":{"objective":"my_objective"}}
```

Parameters:
- `objective`: The internal name of the scoreboard objective

Example output: `My Objective`

---

**Scoreboard Objective Criteria (scoreboard_objective_criteria)**

Returns the criteria of a scoreboard objective (e.g. `dummy`, `playerKillCount`).

```json
{"placeholder":"scoreboard_objective_criteria","values":{"objective":"my_objective"}}
```

Parameters:
- `objective`: The name of the scoreboard objective

Example output: `dummy`

---

**Scoreboard Objective Render Type (scoreboard_objective_render_type)**

Returns the render type of a scoreboard objective — either `integer` or `hearts`.

```json
{"placeholder":"scoreboard_objective_render_type","values":{"objective":"my_objective"}}
```

Parameters:
- `objective`: The name of the scoreboard objective

Example output: `integer`

---

**Scoreboard Player Scores List (scoreboard_player_scores_list)**

Returns a formatted, separated list of all scores a player has across every objective.

```json
{"placeholder":"scoreboard_player_scores_list","values":{"player":"Player1","separator":", ","format":"%objective%: %score%"}}
```

Parameters:
- `player`: The name of the score holder
- `separator`: Text to join entries with (default: `", "`)
- `format`: Format string for each entry. Use `%objective%` and `%score%` as placeholders (default: `%objective%: %score%`)

Example output: `my_objective: 42, kills: 7, deaths: 3`

---

**Scoreboard Objective Count (scoreboard_objective_count)**

Returns the total number of scoreboard objectives.

```json
{"placeholder":"scoreboard_objective_count"}
```

Example output: `3`

---

**Player Team (player_team)**

Returns the scoreboard team name of the specified player. Returns empty if the player is not on a team.

```json
{"placeholder":"player_team","values":{"player_name":"Joel4848"}}
```

Parameters:
- `player_name`: The name of the player to look up

Example output: `Pandas`

---

**Players With Score (players_with_score)**

Returns a separated list of all score holders whose score for a given objective matches a value or range. Uses the same range syntax as Minecraft commands.

```json
{"placeholder":"players_with_score","values":{"objective":"my_objective","score":"1..","separator":", "}}
```

Parameters:
- `objective`: The name of the scoreboard objective
- `score`: A score value or range:
    - `5` — exactly 5
    - `3..7` — between 3 and 7 inclusive
    - `3..` — 3 or above
    - `..7` — 7 or below
- `separator`: Text to join names with (default: `", "`)

Example output: `Player1, Player2`

---

### World

---

**Player Has Tag (player_has_tag)**

Returns `true` if the named player has the given tag (as applied by `/tag`), `false` otherwise.

```json
{"placeholder":"player_has_tag","values":{"player_name":"Steve","tag_name":"my_tag"}}
```

Parameters:
- `player_name`: The name of the player to check
- `tag_name`: The tag to look for

Example output: `true`

---

**Player Tags List (player_tags_list)**

Returns a sorted, separated list of all tags on the named player.

```json
{"placeholder":"player_tags_list","values":{"player_name":"Steve","separator":", "}}
```

Parameters:
- `player_name`: The name of the player to check
- `separator`: Text to join tag names with (default: `", "`)

Example output: `my_tag, other_tag, third_tag`

## License

[CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/)
