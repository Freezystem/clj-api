(ns clj-api.resources.users)

(defn get-users
  "handle users GET request"
  []
  (prn "GET users")
  "<h1>Get all users</h1>")

(defn post-users
  "handle users POST request"
  [req]
  (prn "POST users")
  (prn req)
  (str "<h1>POST users</h1>"))

(defn get-user
  "handle user GET request"
  [{{id :id} :params}]
  (prn "GET user")
  (str "<h1>Get user " id "</h1>"))

(defn put-user
  "handle user PUT request"
  [{{id :id} :params}]
  (prn "PUT user")
  (str "<h1>Put user " id "</h1>"))

(defn delete-user
  "handle user DELETE request"
  [{{id :id} :params}]
  (prn "DELETE user")
  (str "<h1>Delete user " id "</h1>"))