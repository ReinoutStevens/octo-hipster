octo-hipster
============

Hackathon code for Sattose2012 to make Vadim happy. Septem-hipster was not cool
enough to our liking.

# Installation

So, you'd like to install the small thing we did? That's cute.

* Install Eclipse Juno
* Install https://github.com/cderoove/damp.ekeko
* Checkout https://github.com/ReinoutStevens/damp.scrapperplugin and import it
  as an Eclipse Plugin Project
* Die a little bit inside
* Download the tar-file of the repositories. Note that this file
  is quite large. The file can be found
  [here](http://narwhal.rave.org/~resteven/101repo.tar).
  Extract the tar-file under your Eclipse workspace (or a custom
  workspace not to pollute your own).

You should be able to launch the 'Scrapperplugin' as an Eclipse Application. If
you cannot do so, go back to step 1. 

* Import 101repo (the easiest is to create a new project at the location of
  101repo)
* Rightclick on 101repo and do 'add history nature' and 'include in ekeko
  queries'. You should suddenly see the other projects as well.
* Grab a beer, you'll need it.
* Go to the Ekeko Menu and do 'start nRepl'.
* Go to your original Eclipse and connect to the REPL, either by clicking the
  link in the console or by Window - Connect to nREPL.
* Load ScrapperPlugin/scrapperplugin.clj.logic.clj (rightclick, load in REPL)
* Switch to the file's namespace (rightclick, switch to namespace)


The following code should work. If not you did something wrong. Go back to step
3, repeat as much as needed. Also check that you are actually in the namespace.

```clj

(def model (first (history-project-models)))
(def graph (make-graph model))
(def root (first (all-roots))

```

If you got this far everything should work. Hooray!

# Queries

We have written some very interesting queries (*cough*) that reason over the
history of the repository. We will start by just getting all the file types
that are present in the whole repository (so each version).

```clj

(run* [ext] ;;run a logic query
  (file-extensiono ext)) ;;ext is a file-extension

```

You should get some results now. The next step is all the file-extensions in
the first version.

```clj

(run* [ext]
 (fresh [end] ;;a new logical var
  (qwal graph root end ;;run a query over graph, from root to end
   [] ;;define no new variables that are used in the qwal part
   (qcurrent ;;execute the following stuff in the current version
    [current]
    (file-extensiono  ext))))) ;;get file types in this version

```

In QWAL everything is executed in the current version. So in the previous
example everything is executed in the root version. In the next example we will
look for extensions that are available in all the versions.

```clj

(run* [ext]
 (fresh [end]
  (qwal graph root end
   []
   (q=>+ ;;the following goal must succeed 1 or many times
    (qcurrent
     [current]
     (file-extensiono ext)))
   qendversiono))) ;;the current version has no successors

```

As a final example we will look which filetypes are added in the last version.
Note that this query does not return any results, as we only use 5 versions
that only contain commits from the same contribution.


```clj

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

```

This query is a bit more complex. First of all, we reason through the repository
backwards. This makes the query a bit easier: we are looking for a version
where an extension is not present, and the extension is added in the successor.
Unfortunately, looking whether something is not present can only be done when
you know what you are actually looking for. So we change the query to first
look for a version where an extension is present, and then look for a
predecessor where the extension is not present.


In general we can ask everything Eclipse provides us. For a normal IProject
this is not too interesting, as you can only ask filenames and its contents,
but you dont get any structural information about the contents (so AST nodes).
For this you'd need a project in a single language that Eclipse can build.
Unfortunately this is not the case with the 101repo project.


# Links

* [ekeko](https://github.com/cderoove/damp.ekeko)
* [qwal](https://github.com/ReinoutStevens/damp.qwal)
* [scrapper](https://github.com/ReinoutStevens/damp.scrapperplugin)
* [101repo](https://github.com/101companies/101repo)


# TODO

Screenshots
