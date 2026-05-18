#!/bin/bash
set -e

echo "Cloning Dify repository..."
git clone https://github.com/langgenius/dify.git /tmp/dify

echo "Copying docker directory..."
cp -r /tmp/dify/docker .

echo "Copying .env.example to .env..."
cp docker/.env.example docker/.env

echo "Cleaning up..."
rm -rf /tmp/dify

echo "Done! Please check the docker/ directory."