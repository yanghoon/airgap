#!/bin/bash
set -e

if [ -z "$1" ]; then
  echo "Usage: $0 <plugin-path>"
  echo "Example: $0 models/openai_api_compatible"
  exit 1
fi

PLUGIN_PATH="$1"
PLUGIN_NAME=$(basename "$PLUGIN_PATH")
ARCHIVE_URL="https://github.com/langgenius/dify-official-plugins/archive/refs/heads/main.zip"
ARCHIVE_FILE="/tmp/dify-official-plugins.zip"
EXTRACTED_DIR="/tmp/dify-official-plugins-main"
PLUGINS_DIR="plugins"

echo "Downloading dify-official-plugins..."
curl -L -o "$ARCHIVE_FILE" "$ARCHIVE_URL"

echo "Extracting plugin: $PLUGIN_PATH"
rm -rf "$EXTRACTED_DIR"
mkdir -p "$EXTRACTED_DIR"
unzip -q -o "$ARCHIVE_FILE" "dify-official-plugins-main/$PLUGIN_PATH/*" -d /tmp

mkdir -p "$PLUGINS_DIR"
cp -r "$EXTRACTED_DIR/$PLUGIN_PATH" "$PLUGINS_DIR/"

echo "Creating package: ${PLUGIN_NAME}.difypkg"
cd "$PLUGINS_DIR"
tar -czf "${PLUGIN_NAME}.difypkg" "$PLUGIN_NAME"
rm -rf "$PLUGIN_NAME"

rm -rf "$ARCHIVE_FILE" "$EXTRACTED_DIR"
echo "Done!"