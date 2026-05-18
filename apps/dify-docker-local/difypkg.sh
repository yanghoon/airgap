#!/bin/bash
set -e

if [ -z "$1" ]; then
  echo "Usage: $0 <plugin-path>"
  echo "Example: $0 models/openai_api_compatible"
  exit 1
fi

PLUGIN_PATH="$1"
PLUGIN_NAME=$(basename "$PLUGIN_PATH")

if [ ! -d "$PLUGIN_PATH" ]; then
  echo "Plugin directory not found locally. Downloading from GitHub..."
  ARCHIVE_URL="https://github.com/langgenius/dify-official-plugins/archive/refs/heads/main.zip"
  ARCHIVE_FILE="/tmp/dify-official-plugins.zip"
  EXTRACTED_DIR="/tmp/dify-official-plugins-main"

  curl -L -o "$ARCHIVE_FILE" "$ARCHIVE_URL"
  rm -rf "$EXTRACTED_DIR"
  mkdir -p "$EXTRACTED_DIR"
  unzip -q -o "$ARCHIVE_FILE" "dify-official-plugins-main/$PLUGIN_PATH/*" -d "$EXTRACTED_DIR"
  PLUGIN_SRC="$EXTRACTED_DIR/dify-official-plugins-main/$PLUGIN_PATH"
  PLUGIN_PARENT=$(dirname "$PLUGIN_PATH")
  if [ -n "$PLUGIN_PARENT" ] && [ "$PLUGIN_PARENT" != "." ]; then
    mkdir -p "$PLUGIN_PARENT"
  fi
  mv "$PLUGIN_SRC" "./$PLUGIN_PATH"
  rm -rf "$ARCHIVE_FILE" "$EXTRACTED_DIR"
fi

if ! command -v dify &> /dev/null; then
  echo "Dify CLI not found. Installing..."

  if command -v brew &> /dev/null; then
    echo "Installing via Homebrew..."
    brew tap langgenius/dify
    brew install dify
  else
    echo "Homebrew not found. Installing Dify CLI manually..."
    ARCH=$(uname -m)
    if [ "$ARCH" = "arm64" ]; then
      BINARY="dify-plugin-darwin-arm64"
    else
      BINARY="dify-plugin-darwin-amd64"
    fi

    TEMP_DIR=$(mktemp -d)
    curl -L -o "$TEMP_DIR/$BINARY" "https://github.com/langgenius/dify/releases/latest/download/$BINARY"
    chmod +x "$TEMP_DIR/$BINARY"
    sudo mv "$TEMP_DIR/$BINARY" /usr/local/bin/dify
    rm -rf "$TEMP_DIR"
  fi

  echo "Verifying installation..."
  dify version
fi

echo "Creating package: ${PLUGIN_NAME}.difypkg"
dify plugin package "./$PLUGIN_PATH"

echo "Done! Package created: ${PLUGIN_NAME}.difypkg"