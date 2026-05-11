
```bash
zarf package create
zarf package deploy zarf-package-trino-iceberg-package-amd64-1.42.1.tar.zst
```

```bash
zarf tools kubectl exec deploy/trino-coordinator -n trino -c trino-coordinator -it -- trino  # show catalogs;
```
