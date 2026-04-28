#!/bin/bash

# zarf dev find-images
IMAGES=(
  goharbor/harbor-core:v2.13.5
  goharbor/harbor-jobservice:v2.13.5
  goharbor/harbor-portal:v2.13.5
  goharbor/harbor-registryctl:v2.13.5
  goharbor/redis-photon:v2.13.5
  goharbor/registry-photon:v2.13.5
)

TMP_DIR=.tmp

## init
mkdir -p "$TMP_DIR"

## functions
tar_file_name() {
  echo "$1.tar" | tr ":/" "-"
}

download() {
  while IFS= read -r image; do
    tar_file="$TMP_DIR/$(tar_file_name $image)"
    echo "Pulling $image -> $tar_file"

    zarf tools registry pull --format=tarball \
      "$image" "$tar_file"
    # zarf tools registry pull "$image"
    # zarf tools registry pull "$image" --format=tarball > "$tar_file"
  done <<< "$(printf '%s\n' "${IMAGES[@]}")"
}

clean() {
  while IFS= read -r image; do
    tar_file="$TMP_DIR/$(tar_file_name $image)"

    if [ -f "$tar_file" ]; then
      echo "Removing $tar_file"
      rm "$tar_file"
    else
      echo "Skipping $tar_file (not exist)"
      continue
    fi
  done <<< "$(printf '%s\n' "${IMAGES[@]}")"
}

files() {
  while IFS= read -r image; do
    tar_file="$TMP_DIR/$(tar_file_name $image)"
    echo "- source: $tar_file"
    echo "  target: /var/lib/rancher/rke2/agent/images/$tar_file"
  done <<< "$(printf '%s\n' "${IMAGES[@]}")"
}

## main
case "$1" in
  "download")
    download
    ;;
  "clean")
    clean
    ;;
  "files")
    files
    ;;
  *)
    echo "Usage: $0 {download|clean|files}"
    exit 1
    ;;
esac
