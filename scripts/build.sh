#!/bin/sh

set -exuo pipefail

docker build \
       -t graalvm \
       -f scripts/Dockerfile \
       .

mkdir -p resources/binaries/

docker run \
       --rm \
       -v $HOME/.m2:/root/.m2 \
       -v $(pwd):/app \
       -w /app \
       graalvm \
       ./scripts/native-image.sh || true

mv target/waste-calendar.linux resources/binaries/

clojure -M:native-image --image-name waste-calendar.macos
mv target/waste-calendar.macos resources/binaries/
