package com.whbnd.canvascompose

sealed interface LineType {
    object  Normal: LineType
    object  FiveStep: LineType
    object  TenStep: LineType
}