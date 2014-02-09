(ns records.unannotated
  (:require [clojure.core.typed :as t]))

(t/ann-datatype X [x :- Number])
(defrecord X [x])

(X. 1)
(X. 1)
