(ns clarifai-clj.core-test
  (:require [clojure.test :refer :all]
            [clarifai-clj.core :refer :all]))

;; TODO: Move to env-vars
(def api-key "ebc31d071d23414ab7e369c003e3c3bf")

;; ------------------------------------------------------------
;; Predict API image: test constants
;; ------------------------------------------------------------

(def valid-image-url "https://samples.clarifai.com/metro-north.jpg")

(def invalid-image-url "https://samples.clarifai.com/1234566789.jpg")

(def expected-concepts
  '({:name "train", :value 0.9987074} {:name "railway", :value 0.9971303} {:name "transportation system", :value 0.9954438} {:name "locomotive", :value 0.9914662} {:name "station", :value 0.9910724} {:name "travel", :value 0.98730266} {:name "subway system", :value 0.9797999} {:name "commuter", :value 0.9676262} {:name "traffic", :value 0.9670719} {:name "railroad track", :value 0.96478844} {:name "blur", :value 0.964049} {:name "urban", :value 0.95840275} {:name "no person", :value 0.95792305} {:name "platform", :value 0.957827} {:name "business", :value 0.95676255} {:name "track", :value 0.9446391} {:name "city", :value 0.9392204} {:name "fast", :value 0.93650824} {:name "road", :value 0.93066376} {:name "terminal", :value 0.91909647}))

;; ------------------------------------------------------------
;; Predict API image: tests
;; ------------------------------------------------------------

(deftest valid-image-url-concepts
  (testing "Get concepts for a valid image url?"
    (is (= expected-concepts
           (concepts api-key :general valid-image-url)))))

(deftest invalid-image-url-exception
  (testing "Throw exception on invalid image url?"
    (is (thrown-with-msg?
          Exception
          (re-pattern invalid-image-url-error-message)
          (concepts api-key :general invalid-image-url)))))

;; ------------------------------------------------------------
;; Predict API video: test constants
;; ------------------------------------------------------------

(def valid-video-url "https://samples.clarifai.com/beer.mp4")

(def invalid-video-url "https://samples.clarifai.com/123456789.mp4")

(def valid-video-model :general)

(def invalid-video-model :demographics)

(def expected-video-concepts-first-frame
  {:frame {:index 0, :time 0},
   :concepts '({:name "drink", :value 0.9850012} {:name "glass", :value 0.9776815} {:name "alcohol", :value 0.9754255} {:name "foam", :value 0.9675237} {:name "beer", :value 0.96635455} {:name "bar", :value 0.9625554} {:name "liquid", :value 0.95224977} {:name "desktop", :value 0.92640626} {:name "refreshment", :value 0.89855886} {:name "bubble", :value 0.89765644} {:name "no person", :value 0.8937441} {:name "luxury", :value 0.892705} {:name "lager", :value 0.8809308} {:name "gold", :value 0.8807614} {:name "closeup", :value 0.8775041} {:name "celebration", :value 0.8748201} {:name "mug", :value 0.85994077} {:name "bottle", :value 0.8575047} {:name "table", :value 0.85627186} {:name "wood", :value 0.8555566})})

(def expected-video-concepts-second-frame
  {:frame {:index 1, :time 1000},
   :concepts '({:name "drink", :value 0.9850012} {:name "glass", :value 0.9776815} {:name "alcohol", :value 0.9754255} {:name "foam", :value 0.9675237} {:name "beer", :value 0.96635455} {:name "bar", :value 0.9625554} {:name "liquid", :value 0.95224977} {:name "refreshment", :value 0.89855886} {:name "bubble", :value 0.89765644} {:name "no person", :value 0.8937441} {:name "lager", :value 0.8809308} {:name "gold", :value 0.8769187} {:name "mug", :value 0.85994077} {:name "desktop", :value 0.84907854} {:name "party", :value 0.84894526} {:name "cold", :value 0.8484639} {:name "pub", :value 0.8477144} {:name "wood", :value 0.8432857} {:name "full", :value 0.8336983} {:name "celebration", :value 0.83069783})})

(def expected-video-concepts-last-frame
  {:frame {:index 8, :time 8000},
   :concepts '({:name "foam", :value 0.9974463} {:name "beer", :value 0.9749906} {:name "no person", :value 0.9728426} {:name "drink", :value 0.96293694} {:name "dark", :value 0.9172268} {:name "mug", :value 0.91442406} {:name "refreshment", :value 0.9136784} {:name "food", :value 0.9088253} {:name "cold", :value 0.9061986} {:name "full", :value 0.9023689} {:name "coffee", :value 0.8999864} {:name "bar", :value 0.8966465} {:name "glass", :value 0.8953383} {:name "delicious", :value 0.8918562} {:name "breakfast", :value 0.8904705} {:name "wood", :value 0.88253486} {:name "lager", :value 0.8731867} {:name "table", :value 0.87307113} {:name "soap", :value 0.8652384} {:name "liquid", :value 0.8634106})})

;; ------------------------------------------------------------
;; Predict API video: tests
;; ------------------------------------------------------------

(deftest valid-video-url-concepts-first-frame
  (testing "Get concepts for a valid video url?"
    (is (= expected-video-concepts-first-frame
           (first (video-concepts api-key :general valid-video-url))))))

(deftest valid-video-url-concepts-second-frame
  (testing "Get concepts for a valid video url?"
    (is (= expected-video-concepts-second-frame
           (second (video-concepts api-key :general valid-video-url))))))

(deftest valid-video-url-concepts-last-frame
  (testing "Get concepts for a valid video url?"
    (is (= expected-video-concepts-last-frame
           (last (video-concepts api-key :general valid-video-url))))))

(deftest invalid-video-model-exception
  (testing "Throw exception when model provided is not valid for video?"
    (is (thrown-with-msg?
          Exception
          (re-pattern invalid-video-model-message)
          (video-concepts api-key invalid-video-model valid-video-url)))))

(deftest valid-video-model?-valid-model
  (is (valid-video-model? valid-video-model)))

(deftest valid-video-model?-invalid-model
  (is (false? (valid-video-model? invalid-video-model))))

(deftest invalid-video-url-exception
  (testing "Throw exception on invalid video url?"
    (is (thrown-with-msg?
          Exception
          (re-pattern invalid-video-url-error-message)
          (video-concepts api-key valid-video-model invalid-video-url)))))

