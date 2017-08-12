(ns clj-api.resources.todos
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clojure.spec.alpha :as s]
            [cheshire.core :refer [generate-string
                                   parse-string]]
            [cheshire.generate :refer [add-encoder
                                       encode-str]]
            [ring.util.response :refer [response
                                        not-found
                                        created
                                        status]])
  (:import (org.bson.types ObjectId)))

(def ^:private db (mg/get-db (mg/connect) "clj-api"))
(def ^:private coll "todos")

(defn get-url-from-request
  "Get URL from request"
  [req]
  (str (name (:scheme req)) "://" (get-in req [:headers "host"]) (:uri req)))

(defn object-id?
  "is id a valid mongo ObjectId"
  [id]
  (ObjectId/isValid id))

;;;;;;;;;;;;;;;;
;; TODO SPECS ;;
;;;;;;;;;;;;;;;;

(s/def ::label string?)
(s/def ::completed boolean?)
(s/def ::owner object-id?)
(s/def ::todo (s/keys :req-un [::label ::completed]
                      :opt-un [::owner]))
(s/def ::p-todo (s/keys :opt-un [::label ::completed]))

;;;;;;;;;;;;;;
;; FIXTURES ;;
;;;;;;;;;;;;;;

(defn create-fixtures
  "insert rows in todos collection"
  []
  (mc/insert-batch db coll
                   [{:label "learn clojure" :completed false}
                    {:label "learn es6" :completed true}
                    {:label "learn html & css" :completed true}]))

(defn empty-todos [] (mc/drop db coll))

;;;;;;;;;;;;;;;;;;
;; HTTP METHODS ;;
;;;;;;;;;;;;;;;;;;

(defn get-todos
  "handle todos GET request"
  [req]
  (response {:todos (mc/find-maps db coll)}))

(defn post-todos
  "handle todos POST request"
  [{{id :id} :params :as req}]
  (let [todo (-> (:params req)
                 (select-keys [:label :owner])
                 (assoc :completed false))
        res (when (s/valid? ::todo todo)
              (mc/insert-and-return db coll todo))]
    (if (nil? res)
      (status {:error "fail to validate todo"} 400)
      (created (:uri req) {:created (:_id res)}))))

(defn get-todo
  "handle todo GET request"
  [{{id :id} :params}]
  (let [todo (when (object-id? id)
               (mc/find-map-by-id db coll (ObjectId. id)))]
    (if (empty? todo)
      (not-found {:error "not found"})
      (response {:todo todo}))))

(defn put-todo
  "handle todo PUT request"
  [{{id :id} :params :as req}]
  (if (object-id? id)
    (let [todo (select-keys (:params req) [:label :completed :owner])
          res (when (s/valid? ::todo todo)
                (mc/update-by-id db coll (ObjectId. id) todo))]
      (if (or (nil? res)
              (zero? (.getN res)))
        (status {:error "fail to validate todo"} 400)
        (response {:updated id})))
    (not-found {:error "not found"})))

(defn patch-todo
  "handle todo PATCH request"
  [{{id :id} :params :as req}]
  (if (object-id? id)
    (let [todo (select-keys (:params req) [:label :completed])
          res (when (and (not (empty? todo))
                         (s/valid? ::p-todo todo))
                (mc/update-by-id db coll (ObjectId. id) {:$set todo}))]
      (if (or (nil? res)
              (zero? (.getN res)))
        (status {:error "fail to validate todo"} 400)
        (response {:patched id})))
    (not-found {:error "not found"})))

(defn delete-todo
  "handle todo DELETE request"
  [{{id :id} :params}]
  (let [res (when (object-id? id)
              (mc/remove-by-id db coll (ObjectId. id)))]
    (if (or (nil? res)
            (zero? (.getN res)))
      (not-found {:error "not found"})
      (response {:deleted id}))))