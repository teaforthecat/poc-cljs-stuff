(ns consulate.core
  (:require [reagent.core :as reagent]
            [consulate.backend :as backend]
            [reagent.session :as session]
            [consulate.utils :refer [log color-states color-op-states operational-states]]
            [consulate.routes :as routes]))



(defonce app-state (reagent/atom
                    {:datacenters [] } ))

(defn update-datacenters [response]
  (let [_datacenters response]
    (swap! app-state
           update-in [:datacenters] (constantly _datacenters))))

(defn get-datacenters []
  (backend/get-datacenters update-datacenters))



(defn handle-path-change [path]
  (let [{:keys [handler message]} (routes/get-handler path)]
    (log message)
    (log handler)
    handler))

(defn root-path [] "/")

(defn image-header-logo []
  [:img {:alt "Consulate Dashboard Logo"
         :border "0"
         :src "images/consulate_logo.png"
         :title "&amp;lt; Back to Dashboard"
         :width "300em"}])

(def image-spacer
  [:img.spacer {:src "images/spacer.png"}])

(def image-opcsprite
  [:img.opcsprite {:src "images/opstate.png"}])

(defn status-text [text color]
  [:div.statustext
   [:p.status "Status:  "]
   [:p.status {:class color} text ]])

(defn opstate-text [text color]
  [:div.optext
   [:p.opstate "OP State:  "]
   [:p.status {:class color} text ]])

(defn detail-buttons []
  [:div.detail_buttons
   [:input.detail_button {:type "Submit"}]])

(defn datacenter [dc]
  (let [name (:name dc)
        stx (:status-text dc)
        otx (:opstate-text dc)
        color (or (get color-states stx) "unknown")
        op_color (or (get color-op-states otx ) "unknown")
        op_state (or (get operational-states otx) "unknown")]
    [:div.div.datacenter ; todo swap div for datacenter in css
     [:div.dash_box
      [:div.opc_holder
       [:div.span.opc {:class op_state}
        image-spacer
        image-opcsprite]]
      [:div.h1 {:class color} name]
      (status-text stx color)
      (opstate-text otx op_color)
      (detail-buttons)]]))

(defn header []
  [:div.header
    [:div.consulate_logo
     [:a {:href (root-path)}
      (image-header-logo)]]])

(defn index-page [doc]
  (into [:div.flexcontainer.wrap.column] ;;todo allow a "datacenters" div in css to contain them
        (map datacenter (:datacenters doc))))

(defn detail-page [doc]
  [:div.content.flexChild.rowParent

   [:div.flexChild {:id "rowUpstream"}

    [:div.flexChild {:id "columnChild86333"}
     [:p.titles
      [:a {:href "/"} "Parents / Upstream"]]]

    [:div.flexChild {:id "columnChild75902"}
     [:p.titles
      [:a {:href "/"} "Another Process"]]]]


    [:div.flexChild {:id "rowDetailView"}
     [:div.dash_box
      [:div.opc_holder
       [:div.span.opc {:class "Running"}
        image-spacer
        image-opcsprite]]
      [:div.h1 {:class "green"} "Eden Prairie"]
      (status-text "Healthy" "green")
      (opstate-text "Running" "green")
      (detail-buttons)]]

   [:div.flexChild {:id "rowDownstream"}

    [:div.flexChild {:id "columnChild86333"}
     [:p.titles
      [:a {:href "/"} "Children / Downstream"]]]

    [:div.flexChild {:id "columnChild86333"}
     [:p.titles
      [:a {:href "/"} "A process Name"]]]]])

(def handlers {:index-page #'index-page
               :detail-page #'detail-page})

(defn current-page []
  (let [loc (or (session/get :path) (str js/window.location))
        handler (:handler (routes/get-handler loc))]
    (get handlers handler)))

(defn page []
  [:div.wrapper
   [header]
   [(current-page)  @app-state]])

(defn init []
  "post-render data setup"
  (get-datacenters))


(defn ^:export main []
  (reagent/render [page]
                  (.getElementById js/document "app"))
  (init))
