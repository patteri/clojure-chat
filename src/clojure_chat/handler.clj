(ns clojure-chat.handler
  (:use org.httpkit.server
        [clojure.data.json :only [json-str]]
        (compojure [core :only [defroutes GET POST]]
                   [handler :only [site]]
                   [route :only [files not-found]])))

; JSON headers
(def ^{:const true} json-header {"Content-Type" "application/json; charset=utf-8"})

; registered clients
(def clients (atom {})) 

(defn register [req]
  (with-channel req channel
    (swap! clients assoc channel req)
    ; send welcome message
    (send! channel {:status 200
                      :headers json-header
                      :body (json-str
                              {:msg "Welcome to the QuickChat!" :author "ChatMaster"})})
    (on-close channel (fn [status]
                        (swap! clients dissoc channel)))))

; handles received messages by sending them to all clients
(defn on-mesg-received [req]
  ; construct response data
  (let [{:keys [msg author]} (-> req :params)
        data {:msg msg :author author}]
    ; send message to the clients
    (doseq [channel (keys @clients)]
      (send! channel {:status 200
                      :headers json-header
                      :body (json-str data)}))
    {:status 200 :headers {}}))

; routing rules
(defroutes app-routes
  (GET "/register" [] register) ; register to the chat using websocket
  (POST "/msg" [] on-mesg-received) ; post a message
  (files "" {:root "static"})
  (not-found "<p>Page not found.</p>"))

; start the server
(run-server (site #'app-routes) {:port 8080})