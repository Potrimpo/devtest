(ns lambdawerk.database-conn
  (:import
    (com.mchange.v2.c3p0 ComboPooledDataSource))
  (:use
    [environ.core :only [env]]))

; setup database spec and connection pool

(defn- pool
  []
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (env :classname))
               (.setJdbcUrl
                 (str "jdbc:" (env :subprotocol) ":" (env :subname) "/" (env :dbname)))
               (.setUser (env :user))
               (.setMaxIdleTimeExcessConnections (* 1 60))
               (.setMaxIdleTime (* 1 60)))]
    {:datasource cpds}))

(def pooled-db (delay (pool)))

(defn db-connection [] @pooled-db)

