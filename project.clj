(defproject lambdawerk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [environ "1.1.0"]
                 [com.taoensso/tufte "1.1.2"]
                 [org.clojure/java.jdbc "0.7.1"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [yesql "0.5.3"]
                 [com.mchange/c3p0 "0.9.5.2"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.2"]]
  :plugins [[lein-environ "1.0.0"]]
  :profiles {})