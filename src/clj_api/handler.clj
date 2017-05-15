(ns clj-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body
                                          wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]
            [clj-api.resources.todos :refer [get-todos
                                             post-todos
                                             get-todo
                                             put-todo
                                             delete-todo]]))

(defroutes app-routes
           (GET "/" [] "<h1>Clojure REST API</h1>")
           (context "/todos" []
             (GET "/" [] get-todos)
             (POST "/" [] post-todos))
           (context "/todo/:id" [todo-id]
             (GET "/" [] get-todo)
             (PUT "/" [] put-todo)
             (DELETE "/" [] delete-todo))
           (route/not-found "<h1>Page Not Found</h1>"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-json-response {:pretty true})
      (wrap-defaults api-defaults)))
