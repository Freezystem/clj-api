(ns clj-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [not-found
                                        response]]
            [ring.middleware.json :refer [wrap-json-body
                                          wrap-json-params
                                          wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]
            [clj-api.resources.todos :refer [get-todos
                                             post-todos
                                             get-todo
                                             put-todo
                                             patch-todo
                                             delete-todo]]))

(defroutes app-routes
           (GET "/" [] (response {:message "Welcome to CLJ-API!"}))
           (context "/todos" []
             (GET "/" [] get-todos)
             (POST "/" [] post-todos))
           (context "/todo/:id" [id]
             (GET "/" [] get-todo)
             (PUT "/" [] put-todo)
             (PATCH "/" [] patch-todo)
             (DELETE "/" [] delete-todo))
           (route/not-found (not-found {:error "Page Not Found"})))

(def app
  (-> app-routes
      (wrap-keyword-params)
      (wrap-json-body {:keywords? true})
      (wrap-json-params)
      (wrap-json-response {:pretty true})
      (wrap-defaults api-defaults)))
