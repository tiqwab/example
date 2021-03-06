### 有限状態機械 (Finite State Machine, Finite Automaton)

- 与えられた文字列が受理可能であるかを示す機械
- 内部状態として現在の状態を持つ
- 現在の状態と入力文字に応じて次の状態へ遷移する
- 開始状態からstartし、受理状態で止まればaccept
- 任意のプログラムを実行できる汎用CPUとは違って、有限オートマトンには、入力に応じてある状態から別の状態への移動方法を決める規則 (rule) の集合がハードコードされています (p64)

この機械が計算できる、というとはじめ腑に落ちない点もあるかもしれない。
けれどこの機械で「与えられた文字列が'ab'ではじまること」や「与えられた文字列の長さが偶数である」等の答えが出せるため、計算していると言える。

#### 決定性有限オートマトン (Deterministic Finite Automaton, DFA)

- 現在の状態と入力文字から次の状態が一意に決定できる

#### 非決定正有限オートマトン (Nondeterministic Finite Automaton, NFA)

- 現在の状態と入力文字から導かれる次の状態の候補が複数存在し得る
  - 'a'を受け取ったときに状態1にも状態2にもいける
  - epsilon遷移
- 一見 DFAより強力に見えるが、受理できる文字列の集合はDFAもNFAも同じ

ある機械が受理できる文字列の集合を言語 (language) と呼ぶ。
DFA, NFA, 正規表現で表すことができる言語は正規言語と呼ばれる。

---

python関連のメモ

### set, frozenset

- setには専用のリテラルがある (e.g. `{1, 2, 3}`)
- 集合のunion, intersect, subtractをそれぞれ `|`, `&`, `-` で表す
- **hashableではない**
  - pythonではhashableとはしていないって話だよね？
  - hashableな集合がほしい場合、immutableなsetとして定義されているfrozensetを使用する
