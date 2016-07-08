package com.eyeball.modlauncher.downloader;

import java.io.File;
import java.net.URL;

public abstract class AssetType {
    public abstract File getAssetDest();

    public abstract URL getAssetSource();
}
