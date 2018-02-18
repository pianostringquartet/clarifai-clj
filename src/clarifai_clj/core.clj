(ns clarifai-clj.core
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clarifai-clj.models :refer [models]]))

;; --------------------------------------------------
;; TODO:
;; - support for Predict API Video endpoint
;; - suport for Search API endpoint (which is image only)
;; - tests for other models?
;; - let clj-http throw "status 400" error on bad-image-urls?
;; - support for images via bytes (not just urls)?
;; --------------------------------------------------

(defn- model-api-endpoint [model]
  (str "https://api.clarifai.com/v2/models/" (model models) "/outputs"))

(defn- post-request [api-key model image-url]
  (client/post
    (model-api-endpoint model)
    {:headers {:authorization (str "Key " api-key)
               :content-type "Application/JSON"}
     :body (json/write-str {:inputs [{:data {:image {:url image-url}}}]})}))

(def invalid-image-url-error-message "Invalid image URL provided.")

(defn- attempt-post-request [api-key model image-url]
  (try
    (post-request api-key model image-url)
    (catch Exception e
      (if (= "clj-http: status 400" (.getMessage e))
        (throw (Exception. invalid-image-url-error-message))
        (throw e))))) ; i.e. something else bad happened!

(defn- response->json [response]
  (json/read-json (:body response true))) ; i.e. :keywordize? true

(defn- json->concepts [json]
  (map #(dissoc % :id :app_id) (-> json
                                  (:outputs)
                                  (first)
                                  (get-in [:data :concepts]))))

(defn concepts
  "Get Predict API model's concepts (and their certainty) for image.

  Returns seq of {:concept-name :concept-certainty} maps.

  api-key (str): Clarifai API Key
  model (keyword): model to use e.g. :general vs. :apparel
  image-url (str): URL where image is hosted"
  [api-key model image-url]
  (-> (attempt-post-request api-key model image-url)
    (response->json)
    (json->concepts)))
