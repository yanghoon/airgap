# Setup

## Opencode

```bash
curl -fsSL https://opencode.ai/install | bash -s -- -v 1.14.20
```

## on Mac

### Zarf

```bash
mkdir -p ~/.brew && curl -L https://github.com/Homebrew/brew/tarball/master | tar xz --strip 1 -C ~/.brew
echo 'eval "$($HOME/.brew/bin/brew shellenv)"' >> ~/.zprofile
eval "$($HOME/.brew/bin/brew shellenv)"

brew tap defenseunicorns/tap
brew install defenseunicorns/tap/zarf@0.73.1
```

### k3d

```bash
brew install kind
kind create cluster --image kindest/node:v1.34.3
# kind get clusters
# kind delete cluster

zarf tools kubectl get node
zarf init --confirm

cd packages/rancher
zarf package create
zarf package deploy --confirm
```
