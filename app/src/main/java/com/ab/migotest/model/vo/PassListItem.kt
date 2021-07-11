package com.ab.migotest.model.vo

import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.model.enums.PassType


sealed class PassListItem {
    class Pass(var content: PassItem) : PassListItem()
    class Separator(val type: PassType) : PassListItem()
}