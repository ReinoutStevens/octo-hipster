(def model (first (history-project-models)))
(def graph (make-graph model))
(def root (first (all-roots))

(run* [ext] ;;run a logic query
  (file-extensiono ext)) ;;ext is a file-extension

(run* [ext]
 (fresh [end] ;;a new logical var
  (qwal graph root end ;;run a query over graph, from root to end
   [] ;;define no new variables that are used in the qwal part
   (qcurrent ;;execute the following stuff in the current version
    [current]
    (file-extensiono  ext))))) ;;get file types in this version

(run* [added-extension]
 (fresh [begin]
  (qwal graph begin root [] ;;we end in root 
   (qcurrent [current] ;;an extension is present
    (fresh [ext]
     (damp.ekeko.workspace.reification/file-extensiono
      added-extension)))
   q<= ;; go to a predecessor
   (qfail ;;and it is no longer present
    (qcurrent
     [current]
     (damp.ekeko.workspace.reification/file-extensiono added-extension))))))


