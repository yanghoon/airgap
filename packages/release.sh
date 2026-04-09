#!/bin/bash

OUT_DIR=/opt/airgap
ZARF_VERSION=v0.74.1

sudo mkdir -p $OUT_DIR/bin
sudo mkdir -p $OUT_DIR/packages
sudo mkdir -p $OUT_DIR/config

sudo cp ./bin/    $OUT_DIR/bin/
sudo cp ./config/ $OUT_DIR/config/

if [ "`zarf version | grep $ZARF_VERSION`" == "" ]; then
  echo "Downloading zarf $ZARF_VERSION"
  curl -sL "https://github.com/zarf-dev/zarf/releases/download/${ZARF_VERSION}/zarf_${ZARF_VERSION}_Linux_amd64"
  chmod +x zarf
  sudo mv zarf $OUT_DIR/bin/
fi

$(cd init-rke2 && zarf package create)

read -srp "RESTIC_PASSWORD: " RESTIC_PASSWORD
echo
export RESTIC_PASSWORD

source ~/.restic_env
restic cat config
restic backup $OUT_DIR
