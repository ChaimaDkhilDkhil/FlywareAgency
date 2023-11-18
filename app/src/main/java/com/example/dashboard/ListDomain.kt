package com.example.dashboard


class ListDomain(title: String?, url: String?) {
    var title: String? = title
    var url: String? = url

    fun ListDomain(title: String?, url: String?) {
        this.title = title
        this.url = url
    }

    fun getTitle1(): String? {
        return title
    }

    fun setTitle1(title: String?) {
        this.title = title
    }

    fun getUrl1(): String? {
        return url
    }

    fun setUrl1(url: String?) {
        this.url = url
    }
}
