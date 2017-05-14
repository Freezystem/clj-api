(ns clj-api.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-api.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "<h1>Clojure REST API</h1>"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))
      (is (= (:body response) "<h1>Page Not Found</h1>")))))