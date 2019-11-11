# 通信規約

## Android　－＞　マイコン

||1|2|3|4|5|6|7|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|機能|START_BYTE|COMMAND|arg|arg|arg|arg|END_BYTE|
|ユーザーIDを設定する|0000 0000|SET_USER_ID|uid|uid|uid|uid|1111 1111|
|ゲーム状態を設定する|0000 0000|SET_GAME_STATE|state||||1111 1111|
|弾ダメージ設定|0000 0000|SET_BULLET_DAMAGE|damage|damage|damage|damage|1111 1111|
|チーム設定|0000 0000|SET_TEAM_ID|tid|tid|tid|tid|1111 1111|

### GAME_STATE

|Code|概要|
|:---:|:---:|
|GAME_START|ゲーム中|
|GAME_STANDBY|ゲーム待機|
|GAME_PAUSE|ポーズ|

### ResponseCode

|Code|概要|
|:---:|:---:|
|200|OK|
|201|NG|

## マイコン　－＞　Android
||1|2|3|4|5|6|7|8|9|10|11|
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|機能|START_BYTE|COMMAND|arg|arg|arg|arg|arg|arg|arg|arg|END_BYTE|
|被弾通知|0000 0000|HIT_NOTIFICATION|uid|uid|uid|uid|damage|damage|damage|damage|1111 1111|

