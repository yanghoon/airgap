#!/bin/bash

# set -euo pipefail
set -e

# vars
pkg="${1:?Require plugin path. (ex. 'packager/plugins/models/openai_api_compatible')}"
pkg_path="$pkg"
pkg_name="$(basename $pkg)"

setup_environment() {
    mkdir -p packager packager/plugins

    if [[ -d packager && "$(ls -A packager)" ]]; then
        echo "Skip to download packager"
    else
        curl -L https://github.com/kurokobo/dify-plugin-offline-packager/archive/refs/heads/main.tar.gz | tar -xf - -C packager --strip-components=1
    fi

    if [[ -d packager/plugins && "$(ls -A packager/plugins)" ]]; then
        echo "Skip to download official plugins"
    else
        curl -L https://github.com/langgenius/dify-official-plugins/archive/refs/heads/main.tar.gz | tar -xf - -C packager/plugins --strip-components=1
    fi
}

# run_local_packager() {
download_dify_plugin_cli() {
    # local file="$1"
    (cd packager && uv run scripts/packager.py --local "dummy.difypkg" > /dev/null)
}

edit_dependencies() {
    (cd packager && uv add -u "..." "..." --project "$pkg_path")
}

package_plugin() {
    (cd packager && bin/dify-plugin-* plugin package "$pkg_path" -o difypkg/"$pkg_name".difypkg)
    (cd packager && uv run script/packager.py --local difypkg/"$pkg_name".difypkg)
    cp packager/difypkg/"$pkg_name"-offline.difypkg .
}

main() {
    setup_environment
    download_dify_plugin_cli
    # run_local_packager "dummy.difypkg" || true
    # edit_dependencies
    package_plugin
    # run_local_packager "${pkg_name}.difypkg"
}

main "$@"
