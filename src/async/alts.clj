(ns async.alts
  (:require [clojure.core.typed :refer [ann check-ns cf dotimes> Seqable ann-form]
             :as t]
            [clojure.core.typed.async :refer [Chan chan> Port]]
            [clojure.core.async :as async :refer [<!! >!! chan alts!!]]))

(t/typed-deps clojure.core.typed.async)

(ann fan-in [(Seqable (Port Any)) -> (Chan Any)])
(defn fan-in [ins]
  (let [c (chan> Any)]
    (future (while true
              (let [[x] (alts!! ins)]
                (>!! c x))))
    c))

; The second argument needs `:without [Number]` to accurately refine the type
; of the first branch of the number? test. Otherwise, the first branch of (number? cs-or-n)
; can only be refined to (U Int (Seqable (Chan Any))), not good enough to pass to `repeatedly`.
(ann fan-out [(Port Any) (U t/Int (Extends [(Seqable (Chan Any))] :without [Number])) -> (Seqable (Chan Any))])
(defn fan-out [in cs-or-n]
  (let [cs (if (number? cs-or-n)
             (repeatedly cs-or-n chan)
             cs-or-n)]
    (future (while true
              (let [x (<!! in)
                    pair-x (ann-form #(vector % x) [(Chan Any) -> '[(Chan Any) Any]])
                    outs (map pair-x cs)]
                (alts!! outs))))
    cs))

(let [cout (chan> Any)
      cin (fan-in (fan-out cout (repeatedly 3 chan)))]
  (dotimes> [n 10]
    (>!! cout n)
    (prn (<!! cin))))
