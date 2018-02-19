# clarifai-clj

Clojure wrapper for [Clarifai](https://www.clarifai.com/) API.

NOTE: Currently limited to: 

* Predict API (i.e. no Search, Train, etc. APIs) and
* hosted images and videos (i.e. you must provide an image or video URL).

## Usage

Add clarifai-clj to your Leiningen `project.clj` as a `:dependency`:

```scheme
[clarifai-clj "1.0.0"]
```

Include clarifai-clj in your namespace:

```
(:require [clarifai-clj.core :as clarifai])
```

Getting concepts for an image:

```scheme
(def my-api-key "123456789") ; use your real one :-)

(def model-to-use :general) ; see clarifai-clj.models for supported models list

(def image-to-process "https://samples.clarifai.com/metro-north.jpg")

(clarifai/concepts my-api-key model-to-use image-to-process))
;=> ({:name "train", :value 0.9987074} 
;    {:name "railway", :value 0.9971303} 
;    {:name "transportation system", :value 0.9954438} 
;    {:name "locomotive", :value 0.9914662} 
;     ...)
```

Getting concepts for a video:

```scheme
(def video-to-process "https://samples.clarifai.com/beer.mp4")

(def concepts-from-video 
  (clarifai/video-concepts my-api-key model-to-use image-to-process)))
  
;; Clarifai provides a list of concepts for each video frame:
(first (concepts-from-video))
;=> {:frame {:index 0, :time 0},
;    :concepts ({:name "drink", :value 0.9850012} 
;               {:name "glass", :value 0.9776815} 
;               {:name "alcohol", :value 0.9754255} 
;                ...)}

(second (concepts-from-video))
;=> {:frame {:index 1, :time 1000},
;    :concepts ({:name "drink", :value 0.9850012} 
;                ...)}

(last (concepts-from-video))
;=> {:frame {:index 8, :time 8000}, 
;    :concepts ({:name "foam", :value 0.9974463} 
;               {:name "beer", :value 0.9749906} 
;               {:name "no person", :value 0.9728426} 
;               {:name "drink", :value 0.96293694}
;                ...)}
```



Note that, for video, Clarifai only supports a subset of its public models.

## Clarifai Predict API documentation

Please visit [Clarifai Predict API](https://www.clarifai.com/developer/guide/predict#images).

## Todo

* add support for pagination
* add support for Search, Train, Evaluate, Feedback and Workflow APIs



## License
    
Eclipse License.
