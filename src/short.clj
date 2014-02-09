(ns short
  (:require [clojure.core.typed :as t]))

(t/ann-protocol ShortP)
(t/defprotocol> ShortP)
