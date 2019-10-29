package com.example.geofort.activities

import com.google.android.gms.maps.model.LatLng


class MapObjects internal constructor(
    internal var title: String,
    internal var pos: LatLng,
    internal var dragable: Boolean
)