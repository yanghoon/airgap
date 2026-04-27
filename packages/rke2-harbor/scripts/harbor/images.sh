
# zarf dev find-images
IMAGES=(
  "goharbor/chartmuseum-photon:v1.10.2"
  "goharbor/clair-adapter-photon:v1.10.2"
  "goharbor/clair-photon:v1.10.2"
  "goharbor/harbor-core:v1.10.2"
  "goharbor/harbor-db:v1.10.2"
  "goharbor/harbor-jobservice:v1.10.2"
  "goharbor/harbor-portal:v1.10.2"
  "goharbor/harbor-registryctl:v1.10.2"
  "goharbor/notary-server-photon:v1.10.2"
  "goharbor/notary-signer-photon:v1.10.2"
  "goharbor/redis-photon:v1.10.2"
  "goharbor/registry-photon:v1.10.2"
)

tar_file_name() {
  echo "$1.tar" | tr ":/" "-"
}

download() {
  while IFS= read -r image; do
    tar_file="$(tar_file_name $image)"
    echo "Pulling $image and saving as $tar_file"

    zarf tools registry pull --format=tarball \
      "$image" "$tar_file"
  done <<< "$(printf '%s\n' "${IMAGES[@]}")"
}

clean() {
  while IFS= read -r image; do
    tar_file="$(tar_file_name $image)"

    if [ -f "$tar_file" ]; then
      echo "Removing $tar_file"
      rm "$tar_file"
    else
      echo "Skipping $tar_file (not exist)"
      continue
    fi
  done <<< "$(printf '%s\n' "${IMAGES[@]}")"
}

case "$1" in
  "download")
    download
    ;;
  "clean")
    clean
    ;;
  *)
    echo "Usage: $0 {download|clean}"
    exit 1
    ;;
esac
