;; Copyright 2008-2012 Red Hat, Inc, and individual contributors.
;;
;; This is free software; you can redistribute it and/or modify it
;; under the terms of the GNU Lesser General Public License as
;; published by the Free Software Foundation; either version 2.1 of
;; the License, or (at your option) any later version.
;;
;; This software is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
;; Lesser General Public License for more details.
;;
;; You should have received a copy of the GNU Lesser General Public
;; License along with this software; if not, write to the Free
;; Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
;; 02110-1301 USA, or see the FSF site: http://www.fsf.org.

(ns immutant.web.test.session-utils
  (:require [immutant.web.core :as core]))

(defn create-mock-session [session-id]
  (let [store (java.util.Hashtable.)]
    (proxy [javax.servlet.http.HttpSession]
        []
      (getAttribute [key]
        (.get store key))
      (removeAttribute [key]
        (.remove store key))
      (setAttribute [key value]
        (.put store key value))
      (invalidate []
        (.clear store)
        nil)
      (getId []
        session-id))))

(def ^{:dynamic true} mock-session nil)

(defn create-mock-request []
  (proxy [javax.servlet.http.HttpServletRequest]
      []
    (getSession []
      mock-session)))

(defn session-fixture
  ([f]
     (session-fixture "an-id" f))
  ([session-id f]
     (binding [mock-session (create-mock-session session-id)
               core/current-servlet-request (create-mock-request)]
       (f))))