(ns clj-api.resources.todos
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.json :refer :all]
            [cheshire.core :refer [generate-string
                                   parse-string]]
            [cheshire.generate :refer [add-encoder
                                       encode-str]]
            [ring.util.response :refer [resource-response
                                        response
                                        not-found
                                        status]])
  (:import (org.bson.types ObjectId)))

(def ^:private db (mg/get-db (mg/connect) "clj-api"))
(def ^:private coll "todos")

(defn create-fixtures
  "insert rows in todos collection"
  []
  (mc/insert-batch db coll
                   [{:label "learn clojure" :completed false}
                    {:label "learn es6" :completed true}
                    {:label "learn html & css" :completed true}]))

(defn empty-todos [] (mc/drop db coll))

(defn get-todos
  "handle todos GET request"
  [req]
  (response {:todos (mc/find-maps db coll)}))

(defn post-todos
  "handle todos POST request"
  [req]
  (prn "POST todos")
  (prn req)
  (str "<h1>POST todos</h1>"))

(defn get-todo
  "handle todo GET request"
  [{{id :id} :params}]
  (let [todo (when (ObjectId/isValid id)
               (mc/find-map-by-id db coll (ObjectId. id)))]
    (if (empty? todo)
      (not-found {:error "not found"})
      (response {:todo todo}))))

(defn put-todo
  "handle todo PUT request"
  [{{id :id} :params}]
  (prn "PUT todo")
  (str "<h1>Put todo " id "</h1>"))

(defn delete-todo
  "handle todo DELETE request"
  [{{id :id} :params}]
  (let [res (when (ObjectId/isValid id)
               (mc/remove-by-id db coll (ObjectId. id)))]
    (if (or (nil? res) (= (.getN res) 0))
      (not-found {:error "not found"})
      (response {:message "ok"}))))