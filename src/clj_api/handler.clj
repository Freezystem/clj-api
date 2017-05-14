(ns clj-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]
            [clj-api.resources.users :refer [get-users
                                             post-users
                                             get-user
                                             put-user
                                             delete-user]]))


(defroutes app-routes
           (GET "/" [] "<h1>Clojure REST API</h1>")
           (context "/users" []
             (GET "/" [] get-users)
             (POST "/" [] post-users))
           (context "/user/:id" [user-id]
             (GET "/" [] get-user)
             (PUT "/" [] put-user)
             (DELETE "/" [] delete-user))
           (route/not-found "<h1>Page Not Found</h1>"))

(def app
  (wrap-defaults app-routes api-defaults))
