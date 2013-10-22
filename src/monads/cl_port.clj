(ns monads.cl-port
  (:require [clojure.core.typed :as t]))

(t/def-alias Monad
  (TFn [[m :< (TFn [[x :variance :covariant]] Any)]]
    '{:bind (All [x y]
              [(m x) [x -> (m y)] -> (m y)])
      :return (All [x]
                [x -> (m x)])}))

(t/ann reader-monad 
       (All [r]
         (Monad 
           (TFn [[w :variance :covariant]]
                [r -> w]))))
(def reader-monad
  {:return (fn [a]
             (fn [_] a))
   :bind (fn [m k]
           (fn [r]             
             (-> (run-reader m r)
                 k
                 (run-reader r))))})

(t/ann ask (All [x]
             [Any -> [x -> x]]))
(defn ask  [_]  identity)

(t/ann asks (All [r w]
              [[r -> w] -> [r -> w]]))
(defn asks [f]
 (fn [env]
    (f env)))

(t/ann run-reader (All [r w]
                    [[r -> w] r -> w]))
(defn run-reader [reader env]
  (reader env))

(t/ann connect-to-db [-> nil])
(defn connect-to-db []
  (domonad (t/inst reader-monad Any)
           [db-uri :- Any, (asks :db-uri)]
           (prn (format "Connected to db at %s" db-uri))))

(t/ann connect-to-db [-> nil])
(defn connect-to-api []
  (domonad (t/inst reader-monad Any)
           [api-key :- Any, (asks :api-key)]
           (prn (format "Connected to api with key %s" api-key))))

(t/ann run-app String)
(def run-app
  (domonad (t/inst reader-monad Any)
           [_ :- Any, (connect-to-db)
            _ :- Any, (connect-to-api)]
           "Done."))

(-> run-app
    (run-reader {:db-uri "user:passwd@host/dbname" :api-key "AF167"})
    prn)
