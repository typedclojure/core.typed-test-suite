(ns test-suite.gen
  (:require [clojure.core.typed :as t]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.generators :as gen]))

(def check-constants
    (prop/for-all [v (gen/vector gen/any-printable)]
      (-> (t/check-form* `(quote ~v))
          :delayed-errors
          empty?)))

(tc/quick-check 20 check-constants)
