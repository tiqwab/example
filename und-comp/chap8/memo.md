### 8.1.1 万能システムはアルゴリズムを実行できる

#### アルゴリズム

- 入力値を出力値に変えるプロセスを記述した命令のリスト
- 問題を解くための手段を定式化したもの

##### 性質

- 有限性 (Finiteness)
  - 命令は有限個である
- 単純性 (Simplicity)
  - 各命令は創意工夫しなくても、人が紙と鉛筆で実行できるくらい単純である
- 停止性 (Termination)
  - 任意の入力について有限回のステップで終わらせることができる
- 正確性 (Correctness)
  - 任意の入力について正しい答えを生成することができる

##### 例

- ユークリッドの互除法

##### チャーチ・チューリングのテーゼ

- 計算可能性に関して広く受け入れられている推測であり、「計算可能な関数」を帰納的関数で表現される関数のクラスと同一視するという考え
  - ラムダ計算で定義できる関数、チューリングマシンで実行できるプログラムのクラスともいえる
- これをアルゴリズムの定義とする見方もある
  - チューリングマシンによって計算可能なものを計算する手続きをアルゴリズムとする

### 8.1.3 コードはデータである

- プログラムは特定のシステムを制御するための命令であるとともに、それ自体がプログラムの入力となることができるデータである

### 8.1.4 万能システムは永久にループできる

- あるシステムが万能性を持つならば停止しない計算を行える

### 8.4 その他の決定不能な問題

- ライスの定理 (Rice's theorem)
  - 任意のプログラムの自明でない性質は決定不能
    - e.g. "HelloWorld" を出力するプログラムか否か
    - e.g. プログラムがバグを含んでいるか否か
      - 一般的に、そしてあらゆる入力に対し正しい答えを返すことが保証されるプログラムを書くことはできない
      - だからこそテストが一つの技法になる
  - 何故かを直感的に理解するには、これが可能だと停止性問題が解決できることを見るのがいい

### 8.5 残念なこと

- 万能性と無限ループに陥らないプログラムを書くことは両立し得ない

以下は Devid Turner 「[Total Functional Programming][1]」 の引用。

> There is a dichotomy in language design, because of the halting problem. For our programming descipline we are forced to choose between
>
> (A) Security - a language in which all programs are known to terminate.
>
> (B) Universality - a language in which we can write
>
> 1. all terminating programs
> 2. silly programs which fail to terminate
>
> and, given an arbitrary program we cannot in general say if it is 1 or 2.

### 8.6 なぜ起こるのか

- 自己参照できるほどの能力を持つシステムは、自己に関する全ての質問に正しく答えることはできません (大雑把に言うと、ゲーデルの第1不完全性定理と同じこと)

[1]: http://www.jucs.org/jucs_10_7/total_functional_programming
