
# Common
sudo ln -sf /opt/airgap/bin/zarf /usr/local/bin/zarf

# for User
mkdir -p ~/.zarf
ln -sf /opt/airgap/config/zarf-config.yaml ~/.zarf/zarf-config.yaml

# for ROOT
sudo mkdir -p /root/.zarf
sudo ln -sf /opt/airgap/config/zarf-config.yaml /root/.zarf/zarf-config.yaml