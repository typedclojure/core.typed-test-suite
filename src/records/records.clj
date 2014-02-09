(ns records.records
  (:require [clojure.core.typed :as t]))

; annotate record as datatype
(t/ann-datatype Foo [])
(defrecord Foo [])
