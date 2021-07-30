#!/bin/sh

clojure -M:build/native-image \
        --graalvm-home=/usr/lib/graalvm/ \
        --image-name waste-calendar.linux && \
    chown -R $(id -u):$(id -g) ./target
