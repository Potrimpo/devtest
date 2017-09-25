(ns lambdawerk.core
  (:require
    [environ.core :refer [env]]
    [taoensso.tufte :as tufte]
    [yesql.core :refer [defquery]]
    [lambdawerk.database-conn :refer [db-connection]]
    [clojure.java.jdbc :as jdbc]
    [lambdawerk.zipper-parsing :refer [parse-xml]]
    [clojure.java.io :as io])
  (:import (java.util.zip GZIPInputStream)))


(defquery insert-or-udpdate-person "sql/insert-or-update-person.sql"
          {:connection (db-connection)})

; with partition-size 10
; runtime 215.37s
; with partition-size 100:
; runtime 199.08s
; with partition size 300:
; runtime 209.2s
; with live decompressing at partition-size 100:
; runtime 214.81s

(defn- member-to-db
  "attempt upsert
  return 0 if there is nothing that needs to change
  return 1 if insert or update succeeds"
  [mem]
  (let [row-success (insert-or-update-person mem)]
    (if (empty? row-success)
      0
      1)))

(defn- process-members-batch
  [mem-partition]
  (->> mem-partition
    (map member-to-db)
    (reduce + 0)))

(defn- stream-file
  []
  (->> (env :update-file-name)
       io/input-stream
       GZIPInputStream.
       io/reader))

(defn main
  "take a lazy seq of members, partition and parallel map over each partition
  adding members into the database, returning a count of affected rows"
  []
  (->> (stream-file)
       parse-xml
       (partition-all 100)
       (pmap process-members-batch)
       (reduce + 0)))

(defn logging-main
  "Run main with profiling enabled"
  []
  (tufte/add-basic-println-handler! {})
  (let [rows (tufte/profile {} (main))]
    (println "Affected rows: " rows)
    rows))
