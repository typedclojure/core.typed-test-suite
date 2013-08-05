(ns test-suite.tests
  (:require [clojure.test :refer :all]
            [clojure.core.typed :as t :refer [check-ns]]))

(deftest async-test
  (is (check-ns 'async.go))
  (is (check-ns 'async.alts))
  (is (check-ns 'async.walkthrough)))
