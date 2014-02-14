(ns hmap.ten-optionals
  (:require [clojure.core.typed :as t]))

(t/def-alias TenOptionals   (HMap :mandatory {} :optional {:a String :b String :c String :d String :e String :f String :g String :h String :i String :j String}))


(t/ann one [TenOptionals -> (t/Option String)])
(defn one [m] (:a m))

(t/ann two [TenOptionals -> (t/Option String)])
(defn two [m] (:a m))

(t/ann three [TenOptionals -> (t/Option String)])
(defn three [m] (:a m))

(t/ann four [TenOptionals -> (t/Option String)])
(defn four [m] (:a m))

(t/ann five [TenOptionals -> (t/Option String)])
(defn five [m] (:a m))

(t/ann six [TenOptionals -> (t/Option String)])
(defn six [m] (:a m))

(t/ann seven [TenOptionals -> (t/Option String)])
(defn seven [m] (:a m))

(t/ann eight [TenOptionals -> (t/Option String)])
(defn eight [m] (:a m))

(t/ann nine [TenOptionals -> (t/Option String)])
(defn nine [m] (:a m))

(t/ann ten [TenOptionals -> (t/Option String)])
(defn ten [m] (:a m))

