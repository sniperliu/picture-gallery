# picture-gallery

It's the project from nice "Web Development with Clojure 2nd Edition".

generated using Luminus version "2.9.10.47"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To initialize the H2 database

    lein run migrate

To start a web server for the application, run:

    lein run

    lein figwheel

    open http://localhost:3000

## Features

   - Single Page Application with Reagent
   - SQL Database Access with HugSQL
   - Web Services with compojure-api/swagger
   - Athentication
   - Fileupload

Copyright © 2016
