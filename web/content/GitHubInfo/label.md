---
title: "label"
date: 2019-09-29T02:31:31+09:00
draft: false
---
ラベルには全体で利用される`Global Label`とプロジェクトごとに利用される`Local Label`の2種類があります。**Global Labelは勝手に定義してはいけません**が、Local Labelは自由に定義できます。ただし、以下の構文に準拠する必要があります。

## Local Labelの構文
```
x/[プロジェクト名]/[任意の文字列]
```
### 注意
- **プロジェクト名は、projectsディレクトリで定義されているプロジェクトのトップディレクトリ名である必要があります。**

### 例
```
x/orlaya/area/calandar
```

## Global Label
Global Labelには以下のような種類があります。種類は順次増える可能性があります。

### kindラベル
kindラベルはissue/PullRequestの種類を表すラベルです。

### priorityラベル
priorityラベルは優先度を表すラベルです。