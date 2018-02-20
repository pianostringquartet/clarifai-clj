(ns clarifai-clj.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clarifai-clj.models :as models]))

;; --------------------------------------------------
;; TODO:
;; - support other Clarifai APIs (e.g. Search API)
;; - support images via bytes (not just urls)
;; - support paging through results
;; --------------------------------------------------

;; ------------------------------------------------------------
;; Predict API constants
;; ------------------------------------------------------------

(defn- model-api-endpoint [model]
  (str "https://api.clarifai.com/v2/models/" (model models/models) "/outputs"))

(def invalid-image-url-error-message "Invalid image URL provided.")

(def invalid-video-url-error-message "Invalid video URL provided.")

(def invalid-video-model-message "Invalid model for video provided.")

;; ------------------------------------------------------------
;; Predict API helpers
;; ------------------------------------------------------------

(defn- request-headers [api-key]
  {:headers {:authorization (str "Key " api-key)
             :content-type "Application/JSON"}})

(defn- request-body [url video-or-image]
  {:body (json/write-str {:inputs [{:data {video-or-image {:url url}}}]})})

(defn- post-request [api-key model url video-or-image]
  (client/post
    (model-api-endpoint model)
    (merge (request-headers api-key)
           (request-body url video-or-image))))

(defn- attempt-post-request [api-key model url video-or-image]
  (try
    (post-request api-key model url video-or-image)
    (catch Exception e
      (if (= "clj-http: status 400" (.getMessage e))
        (throw (Exception.
                (video-or-image {:video invalid-video-url-error-message
                                 :image invalid-image-url-error-message})))
        (throw e))))) ; i.e. something else bad happened!

(defn- response->json [response]
  (json/read-json (:body response true))) ; i.e. :keywordize? true

(defn- clean-concept
  "Make a concept's data friendlier to user."
  [concept-map]
  (dissoc concept-map :id :app_id))

;; ------------------------------------------------------------
;; Predict API image
;; ------------------------------------------------------------

(defn- image-json->concepts
  "Get concepts from Predict API image response's JSON."
  [image-json]
  (map clean-concept (-> image-json
                       (:outputs)
                       (first)
                       (get-in [:data :concepts]))))

(defn concepts
  "Get Predict API model's concepts (and their certainty) for image.

   Returns seq of 'concept maps', where a concept map is:
    `{:name <concept's name (str)>
     :value <concept's certainty (double)>}`

   api-key (str): your Clarifai API Key
   model (keyword): model to use e.g. :general vs. :apparel
   image-url (str): URL where image is hosted"
  [api-key model image-url]
  (-> (attempt-post-request api-key model image-url :image)
    (response->json)
    (image-json->concepts)))

;; ------------------------------------------------------------
;; Predict API video
;; ------------------------------------------------------------

(defn- video-json->frames
  "Get frame data from Predict API video response's JSON.

  Returns a seq of maps,
    where each map represents a frame and its concepts."
  [video-json]
  (:frames (:data (first (second (second video-json))))))

(defn- clean-frame
  "Make a frame's data friendlier to user."
  [frame]
  (let [concepts (map clean-concept
                      (get-in frame [:data :concepts]))]
    {:frame (:frame_info frame)
     :concepts concepts}))

(defn valid-video-model?
  "Is the model valid for video processing?

  Only a subset of public models are supported for video.
  See models/video-models."
  [model]
  (if (model models/video-models) true false))

(defn video-concepts
  "Get Predict API model's concepts (and their certainty) for video.

   Returns seq of 'frame maps', where a frame map is:
    {:frame {:index <(int)> :time <(int)>}
     :concepts <seq of concept maps, where a concept map is:
                        {:name <concept's name (str)>
                         :value <concept's certainty (double)>}>}

   api-key (str): your Clarifai API Key
   model (keyword): model to use e.g. :general vs. :apparel
   video-url (str): URL where video is hosted"
  [api-key model video-url]
  (if-not (valid-video-model? model)
    (throw (Exception. invalid-video-model-message))
    (let [video-json (response->json
                      (attempt-post-request api-key
                                            model
                                            video-url
                                            :video))]
      (map clean-frame (video-json->frames video-json)))))



