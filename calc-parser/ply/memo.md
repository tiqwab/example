# [PLY (Python Lex-Yacc)][1] を使用した数式の構文解析

### ply.lex

- 字句解析を担当
- 用意するのは以下
  - define `tokens`, a list of token names
  - regular exression rules for each token
    - `t_something` という命名にする
      - tok.type: 解析された token の名前
      - tok.value: 解析された token の文字列
      - tok.lineno: 解析された token の行番号
      - tok.lexpos: 解析された token の input 文字列からの相対位置
- 解析に使用される定義の順番は以下
  - token を関数で定義した場合、定義順がそのまま解析時に試される順になる
  - token を正規表現文字列で定義した場合、定義が長いもの? から試される
  - 両方ある場合、関数が先、文字列が後
- 無視したい token に対しては定義した関数内で `pass`
- whitespace や tab を無視するということであれば、 それ用に `t_ignore` がある
  - `t_ignore = r' \t'` のように定義する
- error handling には `t_error` を使用する
- `states` を使用して字句解析時に複数の状態を切り替えることができるようになる

### ply.yacc

- 構文解析を担当
- 用意するのは以下
  - parsing rules
    - `p_something` という命名にする
- はじめに定義されている rule が開始規則となる
  - parser 生成時の start 引数で指定することもできる
- 演算子の associativity と precedence は `precedence` で定義する
- `%prec <name>` という表記で precedence の上書きができる
  - 数式中の `-5` のような表記を parse するのに便利

[1]: http://www.dabeaz.com/ply/

