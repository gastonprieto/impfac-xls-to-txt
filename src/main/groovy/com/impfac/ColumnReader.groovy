package com.impfac


class ColumnReader {
    String column
    String propertyName

    def setValue(record, value) {
        record.@"$propertyName" = value
    }
}
