(ns lambdawerk.zipper-parsing
  (:require
    [clojure.java.io :as io]
    [clojure.zip :as zip]
    [clojure.data.xml :as xml]
    [clojure.data.zip.xml :as zxml]))

(def relevant-tags
  {:firstname     :fname
   :lastname      :lname
   :date-of-birth :dob
   :phone         :phone})

(defn- grab-member-info
  [mem [tag-in-xml column-in-db]]
  (vector column-in-db
          (zxml/xml1->
            mem
            tag-in-xml
            zxml/text)))

(defn- zipped-member->map
  [mem]
  (into {}
        (map (partial grab-member-info mem)
             relevant-tags)))

(defn- xml-node->map
  [node]
  (zxml/xml1-> (zip/xml-zip node)
               :member
               zipped-member->map))

(defn parse-xml
  "extract members information via a zipper"
  [file-seq]
  (->> file-seq
       xml/parse
       :content
       (map xml-node->map)))
