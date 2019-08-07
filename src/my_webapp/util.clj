(ns my-webapp.util)

(defn parse-int
  [n]
  (try 
    (Integer/parseInt n)
    (catch Exception e nil)))
