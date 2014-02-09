(defproject core.typed-test-suite "0.1.0-SNAPSHOT"
  :description "Tests for core.typed"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.typed "0.2.30"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [org.clojure/core.match "0.2.0-rc5"]
                 [org.clojure/tools.macro "0.1.0"]
                 ]

  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :plugins [[lein-typed "0.3.1"]]
  :core.typed {:check [short
                       async.alts
                       async.go
                       async.walkthrough]}

  :profiles {:dev {:repl-options {:port 64450}}})

