- MOD と RANGE が所定の回数の繰り返しで表現できるのは原始再帰関数であるため(p196)
  - 単純にいえばfor-loop が原始再帰関数ということか
  - while が帰納的関数
- 計算モデルとしてチューリングマシンやラムダ計算、帰納的関数といったものがあるが、同じ概念がそれぞれでどう表されているかをまとめるとわかりやすいかも
- 計算を動作として考えるとチューリングマシン、数学的に考えるとラムダ計算、帰納的関数が扱いやすい感じ?
- チャーチ=チューリングのテーゼでは帰納的関数のクラスを計算できる関数として、これがチューリングマシンやラムダ計算で計算可能とするプログラムや関数のクラスと一致するとしている

チューリング完全であるためにはいわゆる 「whileループ」 あるいは「回数を明示的に指定しないような再帰 (正確な表現?)」 の実装が必要になる。

- ラムダ計算では Y コンビネータ で実現
- 帰納的関数では μ再帰関数 で実現
