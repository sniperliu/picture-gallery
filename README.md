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

    open http://localhost:3000/swagger-ui

    open http://localhost:3000/swagger-ui-private

## Features

   - Single Page Application with Reagent
   - Backend page rendering with Selmer
   - Front end pagination
   - SQL Database Access with HugSQL
   - Web Services with compojure-api/swagger
   - Form Validation
   - Athentication with HTTP Basic
   - Fileupload

Copyright © 2016
