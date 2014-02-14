(ns hmap.big-options
  (:use [clojure.core [typed :only [check-ns cf ann ann-many ann-form 
                                    def-alias Seqable Option 
                                    AnyInteger 
                                    fn> doseq>
                                    print-env Seq tc-ignore]]])
  (:require [clojure
             [edn :as edn]
             [set :as set]
             [string :as str]])
  (:import [clojure.lang IPersistentList IPersistentVector 
            IPersistentSet IPersistentMap Keyword Symbol]))



(def-alias SmsDate (U java.sql.Date java.sql.Timestamp nil))

(def-alias SmsMsg (HMap :mandatory {:xt_ptnt_nr Integer
                                    :xt_create_by String
                                    :xt_key_date  (Option SmsDate)
                                    :xt_modif_by    (Option String)
                                    :xt_modif_date  (Option SmsDate)
                                    :xt_event_zvl   (Option String)
                                    :xt_event_date   SmsDate
                                    :xt_event_time   String
                                    :xt_event_descr  (Option String)
                                    :xt_msg          (Option String) 
                                    :st_state        (Option String)
                                    :st_type         (Option String)
                                    :st_create_date  SmsDate
                                    :st_modif_date        SmsDate
                                    :st_num_Modif         (U Integer Long)
                                    :st_submit_date       (Option SmsDate)
                                    :st_submit_deadline   (Option SmsDate)
                                    :st_issues            (U (IPersistentVector String) String)
                                    :st_notif             (U String (Seqable String) nil)
                                    :sms_mobiel           (Option String)
                                    :sms_status           (Option String)
                                    :sms_msg    (Option String) 
                                    }
                       :optional {:xt_event_time_2 String
                                  :xt_event_zvl_2 String
                                  })) 

(def-alias SmsMsgSeq (U nil (Seqable SmsMsg)))


(ann TagMap (IPersistentMap Keyword String))
(def TagMap {:test "hello"})

(ann ^:no-check add-issue [SmsMsg String -> SmsMsg])
(defn add-issue
  "Add an issue to the :st_issues vector of this record."
  [rec issue]
  (assoc rec :st_issues
         (if (:st_issues rec)
           (vec (concat (:st_issues rec) (list issue)))
           (vector issue))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  Function with fixes that passes test
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ann  derive-sms_msg-fmt [SmsMsg -> SmsMsg])
(defn derive-sms_msg-fmt
  "Derive the :sms_msg from the st_notif. :sms_msg should be empty.
  This function PASSES (core.typed/check-ns)"
  [rec]
  (let [res (if-not (seq (:sms_msg rec))
              (let [add-notif (fn> [notif :- (U String nil)] 
                                 (if-let [msg (-> notif (TagMap ))]
                                   (assoc rec :sms_msg (if-let [curr (:sms_msg rec)]
                                                         (str curr "|" msg)
                                                         (str msg)))  ;; <-- core.typed requires str as it thinks msg might be character
                                   (add-issue rec (str "Notificatie " notif " niet herkend"))))
                    notifs (:st_notif rec)]
                ;;(print-env " notifs should be of type (U String (seqable String)): ")
                (if (or (seq? notifs) (vector? notifs))
                  (doseq> [n :- (U String nil) (filter string? notifs)] 
                    (add-notif n))
                  (if (string? notifs)   ;;; core.typed: extra check to satify core.typed (ct inferred it might be a seqable still
                    (add-notif notifs)
                    rec)))
              (add-issue rec "Message is set, can not override"))]
    (if (nil? res)   ;; core-typed requires this additional test
      rec
    res)))
;    res))

(ann  derive-sms_msg-fmt-fail [SmsMsg -> SmsMsg])
(defn derive-sms_msg-fmt-fail
  "Derive the :sms_msg from the st_notif. :sms_msg should be empty.
   This slightly modified function FAILS when running (core.typed/check-ns)
   and produces about 4 kilobytes of text-output as an error-message "
  [rec]
  (let [res (if-not (seq (:sms_msg rec))
              (let [add-notif (fn> [notif :- (U String nil)] 
                                 (if-let [msg (-> notif (TagMap ))]
                                   (assoc rec :sms_msg (if-let [curr (:sms_msg rec)]
                                                         (str curr "|" msg)
                                                         msg))  
                                   (add-issue rec (str "Notificatie " notif " niet herkend"))))
                    notifs (:st_notif rec)]
                (if (or (seq? notifs) (vector? notifs))
                  (doseq> [n :- (U String nil) (filter string? notifs)] 
                    (add-notif n))
                  (if (string? notifs)   ;;; core.typed: extra check to satify core.typed (ct inferred it might be a seqable still
                    (add-notif notifs)
                    rec)))
              (add-issue rec "Message is set, can not override"))]
      res))   ;;;; difference compared to working function 
