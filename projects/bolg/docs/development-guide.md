# Development Guide

## Development Flow
本開発ではアジャイルライクな開発スタイルを採用します。

### Sprint
月〜金をスプリントとして開発を行います。月曜と金曜にはメンバー全員でMTGを行います。

### Check in Meeting
スプリントでやるTODOを決めます(優先度をつけてもらいます)。

### Check out Meeting
月曜にきめたTODOの達成具合を分析します。

## Release Plan
**どの期間までになにを実装するか**決めましょう。
機能はissueの`kind/feature`から選びます。

## Issue Driven
自分のやるタスクはまずissueに登録しましょう。issueでタスク管理する習慣をつけましょう。

## Coding Flow
本開発では[Scaled Trunk-Based Development](https://trunkbaseddevelopment.com/)を採用します。
できるだけ原本を読むことをお勧めしますが、英語に親を殺されたかたもいると思いますので最低限以下のことを理解しておいてください。

- 明確に定義されるブランチは**masterブランチのみ**
- ブランチを切るとき、そのブランチの生存期間が短くなるように切ること
