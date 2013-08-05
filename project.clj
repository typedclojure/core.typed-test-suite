(defproject core.typed-test-suite "0.1.0-SNAPSHOT"
  :description "Tests for core.typed"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.typed "0.1.20"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]]

  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}

  :profiles {:dev {:repl-options {:port 64450}}})

